package io.github.lawkai.jdbc4k

import java.math.BigDecimal
import java.sql.ResultSet
import java.time.Instant

interface IResultSet4k : ResultSet {
    override fun getString(columnLabel: String): String
    fun getStringNullable(columnLabel: String): String?
    override fun getBoolean(columnLabel: String): Boolean
    fun getBooleanNullable(columnLabel: String): Boolean?
    override fun getByte(columnLabel: String): Byte
    fun getByteNullable(columnLabel: String): Byte?
    override fun getShort(columnLabel: String): Short
    fun getShortNullable(columnLabel: String): Short?
    override fun getInt(columnLabel: String): Int
    fun getIntNullable(columnLabel: String): Int?
    override fun getLong(columnLabel: String): Long
    fun getLongNullable(columnLabel: String): Long?
    override fun getFloat(columnLabel: String): Float
    fun getFloatNullable(columnLabel: String): Float?
    override fun getDouble(columnLabel: String): Double
    fun getDoubleNullable(columnLabel: String): Double?
    override fun getBigDecimal(columnLabel: String): BigDecimal
    fun getBigDecimalNullable(columnLabel: String): BigDecimal?
    override fun getBytes(columnLabel: String?): ByteArray
    fun getBytesNullable(columnLabel: String): ByteArray?
    fun getInstant(columnLabel: String): Instant
    fun getInstantNullable(columnLabel: String): Instant?
}
