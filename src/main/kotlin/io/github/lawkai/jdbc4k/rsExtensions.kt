package io.github.lawkai.jdbc4k

import java.sql.ResultSet
import java.time.Instant

/**
 * This method returns an [Instant] given the column label. Note that if column value is SQL Null. It returns
 * [Instant.EPOCH]
 *
 * @param columnLabel column name
 * @return a non-nullable [Instant] of the given [columnLabel]. If database column is SQL Null, it returns
 *     [Instant.EPOCH]
 */
fun ResultSet.getInstant(columnLabel: String): Instant =
    this.getTimestamp(columnLabel)?.toInstant() ?: Instant.EPOCH

/**
 * This method returns an [String] given the column label. It is different from the default [ResultSet.getString] method
 * where it could return a null String!
 *
 * @param columnLabel column name
 * @return the string value of the given column or empty String if the column value is null.
 */
fun ResultSet.getStringNullAsEmpty(columnLabel: String): String =
    this.getString(columnLabel) ?: ""

/**
 * This method returns an [String] given the column label. It is the same as the default [ResultSet.getString] method
 * except that it returns a proper Kotlin nullable String?
 *
 * @param columnLabel column name
 * @return the string value of the given column or null.
 */
fun ResultSet.getStringNullable(columnLabel: String): String? = this.getString(columnLabel)
