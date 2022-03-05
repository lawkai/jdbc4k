package io.github.lawkai.jdbc4k

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.yield
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.sql.Connection
import java.sql.PreparedStatement
import javax.sql.DataSource
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * [DataSource4k] provides suspendable [transaction] and [query] function which allows queries to be run asynchronously.
 *
 * This class uses [limitedParallelism](kotlinx.coroutines.CoroutineDispatcher.limitedParallelism) which is an
 * [ExperimentalCoroutinesApi] as of `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0`.
 *
 * Using this class will require setting compiler flag '-opt-in=kotlin.RequiresOptIn'.
 *
 * ```kotlin
 * // build.gradle.kts
 * kotlin.sourceSets.all {
 *    languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
 * }
 * ```
 *
 * @param dataSource the [DataSource] to use.
 * @param parallelFactor controls how many threads can be run in parallel against the [DataSource]. Note this factor
 *     should not be higher than the number of connections can be provided by the [dataSource]. (ex: if
 *     [dataSource] can handle 10 connections at most, [parallelFactor] should be set at 10 or below.)
 * @param name _Optional_ used to give the [Logger](org.slf4j.Logger) a distinct name. i.e.
 *     _com.github.lawkai.jdbc4k.$name_ if set. Otherwise, the logger
 *     will be named as _com.github.lawkai.jdbc4k.DataSource4k_.
 */
@ExperimentalCoroutinesApi
class DataSource4k(private val dataSource: DataSource, parallelFactor: Int, name: String? = null) {
    private val logger = LoggerFactory.getLogger(
        name?.let {
            "${DataSource4k::class.java.packageName}.$it"
        } ?: DataSource4k::class.java.name,
    )
    private val limitedIOContext = Dispatchers.IO.limitedParallelism(parallelFactor)

    /**
     * Starts a database transaction by setting [autoCommit](java.sql.Connection.setAutoCommit) to `false` and
     * put into the [CoroutineContext]. Any [query] runs within the transaction will reference to the same
     * [Connection] from the [CoroutineContext]. If no exception happened within the [fn] block, it will call
     * [commit](java.sql.Connection.commit) else it will [rollback](java.sql.Connection.rollback) the whole transaction.
     *
     * Nested [transaction] will commit or rollback together with the parent [transaction]
     *
     * A [MDC] _db_connection_ is provided to help _debug_ which [Connection] is being used when running [fn].
     *
     * **Cautions:** calling [CoroutineScope](kotlinx.coroutines.CoroutineScope) `launch` or `async` within the [fn] is
     * not recommended unless you know what you are doing.
     *
     * @param fn function to be run within the transaction block.
     * @return whatever [fn] returns
     */
    suspend fun <T> transaction(fn: suspend CoroutineScope.() -> T): T {
        return coroutineScope {
            when (val connection = coroutineContext.connection) {
                null -> {
                    val conn = dataSource.connection
                    conn.autoCommit = false
                    val connectionContext = ConnectionContext(conn)
                    val result = async(limitedIOContext + connectionContext) {
                        MDC.putCloseable("db_connection", conn.toString()).use {
                            fn()
                        }
                    }
                    result.commitOrRollback(conn)
                    result.await()
                }
                else -> {
                    // for nested transaction
                    MDC.putCloseable("db_connection", connection.toString()).use {
                        fn()
                    }
                }
            }
        }
    }

    /**
     * Runs a query with a [Connection] given by the [DataSource] or from the [CoroutineContext] so that it can be
     * associated to the same [transaction].
     *
     * A [MDC] _db_connection_ is provided to help _debug_ which [Connection] is being used when running [fn].
     *
     * @param sql sql statement for the [fn] [PreparedStatement]
     * @param fn function with [PreparedStatement] and returns anything.
     * @return whatever [fn] returns
     */
    suspend fun <T> query(sql: String, fn: suspend (PreparedStatement4k) -> T): T {
        return coroutineContext.connection?.let { conn ->
            MDC.putCloseable("db_connection", conn.toString()).use {
                // purposely not use `use` to auto close the connection
                fn(PreparedStatement4k(conn.prepareStatement(sql)))
            }
        } ?: dataSource.connection.use { conn ->
            MDC.putCloseable("db_connection", conn.toString()).use {
                fn(PreparedStatement4k(conn.prepareStatement(sql)))
            }
        }
    }

    /**
     * Construct a [Flow] for the given [sql]. The [sql] has to be `readOnly` (i.e. select statement) Please note that
     * the [Connection] used to construct the [Flow] will be closed when the [Flow] terminate.
     *
     * @param sql SQL to run (has to be SELECT statement)
     * @param fn function that takes a [ResultSet4k] to construct an object [T]
     * @return a [Flow] of type [T]
     */
    fun <T> flowQuery(sql: String, fn: (rs: ResultSet4k) -> T): Flow<T> = flow {
        dataSource.connection.use { conn ->
            conn.isReadOnly = true
            conn.prepareStatement(sql).use { ps ->
                ps.executeQuery().use { rs ->
                    while (rs.next()) {
                        this.emit(fn(ResultSet4k(rs)))
                        yield()
                    }
                }
            }
            conn.isReadOnly = false
        }
    }

    private fun Job.commitOrRollback(conn: Connection) = this.invokeOnCompletion { throwable ->
        MDC.putCloseable("db_connection", conn.toString()).use {
            when (throwable) {
                null -> {
                    logger.debug("Connection commit")
                    conn.commit()
                }
                else -> {
                    logger.debug("Connection rollback due to {}", throwable.message)
                    conn.rollback()
                }
            }
            // reset back to autoCommit
            conn.autoCommit = true
            logger.debug("Connection close")
            conn.close()
        }
    }

    private class ConnectionContext(val connection: Connection) : AbstractCoroutineContextElement(ConnectionContext) {
        companion object Key : CoroutineContext.Key<ConnectionContext>
    }

    private val CoroutineContext.connection
        get() = this[ConnectionContext]?.connection
}
