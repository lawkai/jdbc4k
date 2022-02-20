package com.github.lawkai.jdbc4k

import java.sql.PreparedStatement
import java.sql.ResultSet

fun <R> PreparedStatement.executeQuery(fn: (rs: ResultSet) -> R): R? =
    this.executeQuery().use { rs ->
        if (!rs.next()) {
            null
        } else {
            fn(rs)
        }
    }

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

fun <R> PreparedStatement.executeSequenceQuery(fn: (rs: ResultSet) -> R): Sequence<R> {
    val rs = this.executeQuery()
    return sequence {
        while (rs.next()) {
            yield(fn(rs))
        }
        rs.close()
    }
}
