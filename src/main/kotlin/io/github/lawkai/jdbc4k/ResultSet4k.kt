package io.github.lawkai.jdbc4k

import java.math.BigDecimal
import java.sql.ResultSet
import java.time.Instant

internal class ResultSet4k(private val rs: ResultSet) : ResultSet by rs, IResultSet4k {

    override fun getString(columnLabel: String): String =
        rs.getString(columnLabel) ?: ""

    override fun getStringNullable(columnLabel: String): String? =
        rs.getString(columnLabel)

    override fun getBoolean(columnLabel: String): Boolean =
        rs.getBoolean(columnLabel)

    override fun getBooleanNullable(columnLabel: String): Boolean? {
        val result = rs.getBoolean(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getByte(columnLabel: String): Byte =
        rs.getByte(columnLabel)

    override fun getByteNullable(columnLabel: String): Byte? {
        val result = rs.getByte(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getShort(columnLabel: String): Short = rs.getShort(columnLabel)

    override fun getShortNullable(columnLabel: String): Short? {
        val result = rs.getShort(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getInt(columnLabel: String): Int = rs.getInt(columnLabel)

    override fun getIntNullable(columnLabel: String): Int? {
        val result = rs.getInt(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getLong(columnLabel: String): Long = rs.getLong(columnLabel)

    override fun getLongNullable(columnLabel: String): Long? {
        val result = rs.getLong(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getFloat(columnLabel: String): Float =
        rs.getFloat(columnLabel)

    override fun getFloatNullable(columnLabel: String): Float? {
        val result = rs.getFloat(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getDouble(columnLabel: String): Double =
        rs.getDouble(columnLabel)

    override fun getDoubleNullable(columnLabel: String): Double? {
        val result = rs.getDouble(columnLabel)
        return if (rs.wasNull()) null else result
    }

    override fun getBigDecimal(columnLabel: String): BigDecimal =
        rs.getBigDecimal(columnLabel) ?: BigDecimal.ZERO

    override fun getBigDecimalNullable(columnLabel: String): BigDecimal? =
        rs.getBigDecimal(columnLabel)

    override fun getBytes(columnLabel: String?): ByteArray =
        rs.getBytes(columnLabel) ?: ByteArray(0)

    override fun getBytesNullable(columnLabel: String): ByteArray? =
        rs.getBytes(columnLabel)

    override fun getInstant(columnLabel: String): Instant =
        rs.getTimestamp(columnLabel)?.toInstant() ?: Instant.EPOCH

    override fun getInstantNullable(columnLabel: String): Instant? =
        rs.getTimestamp(columnLabel)?.toInstant()
}
