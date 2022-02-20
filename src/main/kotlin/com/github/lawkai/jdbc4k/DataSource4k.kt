package com.github.lawkai.jdbc4k

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.sql.Connection
import javax.sql.DataSource
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * [DataSource4k] provides suspendable [transaction] and [query] function which allows queries to be run asynchronously.
 *
 * This class uses [kotlinx.coroutines.CoroutineDispatcher.limitedParallelism] which is still an
 * `ExperimentalCoroutinesApi` as of `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0`. Using this class will
 * require setting compiler flag '-opt-in=kotlin.RequiresOptIn' unfortunately.
 *
 * @param dataSource the [javax.sql.DataSource] to use.
 * @param parallelFactor controls how many threads can be run in parallel against the [javax.sql.DataSource]. Note this
 *     factor should not be higher than the number of connections can be provided by the [dataSource]. (ex:
 *     if [dataSource] can handle 10 connections at most, [parallelFactor] should be set at 10 or below.)
 * @param name used to give the [org.slf4j.Logger] a distinct name com.github.lawkai.jdbc4k.$name. If not set, the
 *     logger will be named as [com.github.lawkai.jdbc4k.DataSource4k]
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
     * Starts a database transaction by setting [java.sql.Connection.setAutoCommit] to `false` and put into the
     * [kotlin.coroutines.CoroutineContext]. Any [query] runs within the transaction will reference to the same
     * [java.sql.Connection] from the [kotlin.coroutines.CoroutineContext]. If no exception happened within the
     * [fn] block, it will call [java.sql.Connection.commit] else it will [java.sql.Connection.rollback] the whole
     * transaction.
     *
     * Nested [transaction] will commit or rollback together with the parent [transaction]
     *
     * A [org.slf4j.MDC] `db_connection` is provided to help `debug` which [java.sql.Connection] is being used when
     * running [fn].
     *
     * Cautions: calling [kotlinx.coroutines.CoroutineScope] `launch` or `async` within the [fn] is not recommended
     * unless you know what you are doing.
     *
     * @param fn function to be run within the transaction block.
     * @return whatever [fn] returns
     */
    suspend fun <T> transaction(fn: suspend (Connection) -> T): T {
        return coroutineScope {
            when (val connection = coroutineContext.connection) {
                null -> {
                    val conn = dataSource.connection
                    conn.autoCommit = false
                    val connectionContext = ConnectionContext(conn)
                    val result = async(limitedIOContext + connectionContext) {
                        MDC.putCloseable("db_connection", conn.toString()).use {
                            fn(coroutineContext.connection!!)
                        }
                    }
                    result.commitOrRollback(conn)
                    result.await()
                }
                else -> {
                    // for nested transaction
                    MDC.putCloseable("db_connection", connection.toString()).use {
                        fn(connection)
                    }
                }
            }
        }
    }

    /**
     * Runs a query with a [java.sql.Connection] given by the [javax.sql.DataSource] or from the [kotlin.coroutines.CoroutineContext]
     * so that it can be tied to the same [transaction]
     *
     * A [org.slf4j.MDC] `db_connection` is provided to help `debug` which [java.sql.Connection] is being used when running [fn].
     *
     * @param fn SQL query to run
     * @return whatever [fn] returns
     */
    suspend fun <T> query(fn: suspend (Connection) -> T): T {
        return coroutineContext.connection?.let { conn ->
            MDC.putCloseable("db_connection", conn.toString()).use {
                fn(conn) // purposely not use `use` to auto close the connection
            }
        } ?: dataSource.connection.use { conn ->
            MDC.putCloseable("db_connection", conn.toString()).use {
                fn(conn)
            }
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
