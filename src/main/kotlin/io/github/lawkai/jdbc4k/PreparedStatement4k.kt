package io.github.lawkai.jdbc4k

import java.math.BigDecimal
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types
import java.time.Instant

class PreparedStatement4k(private val ps: PreparedStatement) : PreparedStatement by ps {
    override fun setByte(parameterIndex: Int, x: Byte) = ps.setByte(parameterIndex, x)

    fun setByteNullable(parameterIndex: Int, x: Byte?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.TINYINT)
        } else {
            this.setByte(parameterIndex, x)
        }
    }

    override fun setBytes(parameterIndex: Int, x: ByteArray) = ps.setBytes(parameterIndex, x)

    fun setBytesNullable(parameterIndex: Int, x: ByteArray?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.VARBINARY)
        } else {
            this.setBytes(parameterIndex, x)
        }
    }

    override fun setString(parameterIndex: Int, x: String) = ps.setString(parameterIndex, x)

    fun setStringNullable(parameterIndex: Int, x: String?) = ps.setNString(parameterIndex, x)

    override fun setShort(parameterIndex: Int, x: Short) = ps.setShort(parameterIndex, x)

    fun setShortNullable(parameterIndex: Int, x: Short?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.TINYINT)
        } else {
            this.setShort(parameterIndex, x)
        }
    }

    override fun setInt(parameterIndex: Int, x: Int) = ps.setInt(parameterIndex, x)

    fun setIntNullable(parameterIndex: Int, x: Int?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.INTEGER)
        } else {
            this.setInt(parameterIndex, x)
        }
    }

    override fun setLong(parameterIndex: Int, x: Long) = ps.setLong(parameterIndex, x)

    fun setLongNullable(parameterIndex: Int, x: Long?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.BIGINT)
        } else {
            this.setLong(parameterIndex, x)
        }
    }

    override fun setFloat(parameterIndex: Int, x: Float) = ps.setFloat(parameterIndex, x)

    fun setFloatNullable(parameterIndex: Int, x: Float?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.FLOAT)
        } else {
            this.setFloat(parameterIndex, x)
        }
    }

    override fun setDouble(parameterIndex: Int, x: Double) = ps.setDouble(parameterIndex, x)

    fun setDoubleNullable(parameterIndex: Int, x: Double?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.DOUBLE)
        } else {
            this.setDouble(parameterIndex, x)
        }
    }

    override fun setBigDecimal(parameterIndex: Int, x: BigDecimal) = ps.setBigDecimal(parameterIndex, x)

    fun setBigDecimalNullable(parameterIndex: Int, x: BigDecimal?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.DECIMAL)
        } else {
            this.setBigDecimal(parameterIndex, x)
        }
    }

    override fun setBoolean(parameterIndex: Int, x: Boolean) = ps.setBoolean(parameterIndex, x)

    fun setBooleanNullable(parameterIndex: Int, x: Boolean?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.BOOLEAN)
        } else {
            this.setBoolean(parameterIndex, x)
        }
    }

    fun setInstant(parameterIndex: Int, x: Instant) =
        this.setTimestamp(parameterIndex, Timestamp.from(x))

    fun setInstantNullable(parameterIndex: Int, x: Instant?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.TIMESTAMP)
        } else {
            this.setInstant(parameterIndex, x)
        }
    }

    /**
     * Runs the given [PreparedStatement] and parse the [ResultSet4k] with the given [fn] If query does not return any
     * result, it returns null.
     *
     * @param fn function used to read the value from query [ResultSet4k]
     * @return a nullable object of type [R] or null if query returns null.
     */
    fun <R> executeQuery(fn: (rs: ResultSet4k) -> R): R? =
        this.executeListQuery(fn).firstOrNull()

    /**
     * Runs the given [PreparedStatement] and parse the [ResultSet] with the given [fn] If query does not return any
     * result, it returns null.
     *
     * @param fn function used to read the value from query [ResultSet4k]
     * @return a list of type [R] or emptyList if query returns nothing.
     */
    fun <R> executeListQuery(fn: (rs: ResultSet4k) -> R): List<R> =
        this.executeQuery().use { rs ->
            generateSequence {
                if (!rs.next()) {
                    null
                } else {
                    fn(ResultSet4k(rs))
                }
            }.toList()
        }
}
