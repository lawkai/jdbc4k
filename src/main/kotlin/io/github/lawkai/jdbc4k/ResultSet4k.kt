package io.github.lawkai.jdbc4k

import java.math.BigDecimal
import java.sql.ResultSet
import java.time.Instant

class ResultSet4k(private val rs: ResultSet) : ResultSet by rs {

    override fun getString(columnLabel: String): String =
        rs.getString(columnLabel) ?: ""

    fun getStringNullable(columnLabel: String): String? =
        rs.getString(columnLabel)

    override fun getBoolean(columnLabel: String): Boolean =
        rs.getBoolean(columnLabel)

    fun getBooleanNullable(columnLabel: String): Boolean? {
        val result = rs.getBoolean(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getByte(columnLabel: String): Byte =
        rs.getByte(columnLabel)

    fun getByteNullable(columnLabel: String): Byte? {
        val result = rs.getByte(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getShort(columnLabel: String): Short = rs.getShort(columnLabel)

    fun getShortNullable(columnLabel: String): Short? {
        val result = rs.getShort(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getInt(columnLabel: String): Int = rs.getInt(columnLabel)

    fun getIntNullable(columnLabel: String): Int? {
        val result = rs.getInt(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getLong(columnLabel: String): Long = rs.getLong(columnLabel)

    fun getLongNullable(columnLabel: String): Long? {
        val result = rs.getLong(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getFloat(columnLabel: String): Float =
        rs.getFloat(columnLabel)

    fun getFloatNullable(columnLabel: String): Float? {
        val result = rs.getFloat(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getDouble(columnLabel: String): Double =
        rs.getDouble(columnLabel)

    fun getDoubleNullable(columnLabel: String): Double? {
        val result = rs.getDouble(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getBigDecimal(columnLabel: String): BigDecimal =
        rs.getBigDecimal(columnLabel) ?: BigDecimal.ZERO

    fun getBigDecimalNullable(columnLabel: String): BigDecimal? =
        rs.getBigDecimal(columnLabel)

    override fun getBytes(columnLabel: String?): ByteArray =
        rs.getBytes(columnLabel) ?: ByteArray(0)

    fun getBytesNullable(columnLabel: String): ByteArray? =
        rs.getBytes(columnLabel)

    fun getInstant(columnLabel: String): Instant =
        rs.getTimestamp(columnLabel)?.toInstant() ?: Instant.EPOCH

    fun getInstantNullable(columnLabel: String): Instant? =
        rs.getTimestamp(columnLabel)?.toInstant()
}
