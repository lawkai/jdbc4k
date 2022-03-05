package io.github.lawkai.jdbc4k

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant

/**
 * Runs the given [PreparedStatement] and parse the [ResultSet] with the given [fn] If query does not return any result,
 * it returns null.
 *
 * @param fn function used to read the value from query [ResultSet]
 * @return a nullable object of type [R] or null if query returns null.
 */
fun <R> PreparedStatement.executeQuery(fn: (rs: ResultSet) -> R): R? =
    this.executeListQuery(fn).firstOrNull()

/**
 * Runs the given [PreparedStatement] and parse the [ResultSet] with the given [fn] If query does not return any result,
 * it returns null.
 *
 * @param fn function used to read the value from query [ResultSet]
 * @return a list of type [R] or emptyList if query returns nothing.
 */
fun <R> PreparedStatement.executeListQuery(fn: (rs: ResultSet) -> R): List<R> =
    this.executeQuery().use { rs ->
        generateSequence {
            if (!rs.next()) {
                null
            } else {
                fn(rs)
            }
        }.toList()
    }

/**
 * set the [parameterIndex] of the [PreparedStatement] with a value of the [Instant]
 *
 * @param parameterIndex parameter index of the prepared statement.
 * @param instant instant value of the parameter index.
 */
fun PreparedStatement.setInstant(parameterIndex: Int, instant: Instant) =
    this.setTimestamp(parameterIndex, Timestamp.from(instant))
