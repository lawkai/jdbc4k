package io.github.lawkai.jdbc4k

import java.math.BigDecimal
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types
import java.time.Instant

internal class PreparedStatement4k(
    private val namedParameterSql: NamedParameterSql,
    private val ps: PreparedStatement
) : PreparedStatement by ps, IPreparedStatement4k {
    override fun setByte(parameterLabel: String, x: Byte) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setByte(i, x)
        }
    }

    override fun setByte(parameterIndex: Int, x: Byte) = ps.setByte(parameterIndex, x)

    override fun setByteNullable(parameterLabel: String, x: Byte?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setByteNullable(i, x)
        }
    }

    override fun setByteNullable(parameterIndex: Int, x: Byte?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.TINYINT)
        } else {
            this.setByte(parameterIndex, x)
        }
    }

    override fun setBytes(parameterLabel: String, x: ByteArray) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setBytes(i, x)
        }
    }

    override fun setBytes(parameterIndex: Int, x: ByteArray) = ps.setBytes(parameterIndex, x)

    override fun setBytesNullable(parameterLabel: String, x: ByteArray?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setBytesNullable(i, x)
        }
    }

    override fun setBytesNullable(parameterIndex: Int, x: ByteArray?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.VARBINARY)
        } else {
            this.setBytes(parameterIndex, x)
        }
    }

    override fun setString(parameterLabel: String, x: String) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setString(i, x)
        }
    }

    override fun setString(parameterIndex: Int, x: String) = ps.setString(parameterIndex, x)

    override fun setStringNullable(parameterLabel: String, x: String?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setStringNullable(i, x)
        }
    }

    override fun setStringNullable(parameterIndex: Int, x: String?) = ps.setNString(parameterIndex, x)

    override fun setShort(parameterLabel: String, x: Short) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setShort(i, x)
        }
    }

    override fun setShort(parameterIndex: Int, x: Short) = ps.setShort(parameterIndex, x)

    override fun setShortNullable(parameterLabel: String, x: Short?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setShortNullable(i, x)
        }
    }

    override fun setShortNullable(parameterIndex: Int, x: Short?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.TINYINT)
        } else {
            this.setShort(parameterIndex, x)
        }
    }

    override fun setInt(parameterLabel: String, x: Int) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setInt(i, x)
        }
    }

    override fun setInt(parameterIndex: Int, x: Int) = ps.setInt(parameterIndex, x)

    override fun setIntNullable(parameterLabel: String, x: Int?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setIntNullable(i, x)
        }
    }

    override fun setIntNullable(parameterIndex: Int, x: Int?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.INTEGER)
        } else {
            this.setInt(parameterIndex, x)
        }
    }

    override fun setLong(parameterLabel: String, x: Long) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setLong(i, x)
        }
    }

    override fun setLong(parameterIndex: Int, x: Long) = ps.setLong(parameterIndex, x)

    override fun setLongNullable(parameterLabel: String, x: Long?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setLongNullable(i, x)
        }
    }

    override fun setLongNullable(parameterIndex: Int, x: Long?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.BIGINT)
        } else {
            this.setLong(parameterIndex, x)
        }
    }

    override fun setFloat(parameterLabel: String, x: Float) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setFloat(i, x)
        }
    }

    override fun setFloat(parameterIndex: Int, x: Float) = ps.setFloat(parameterIndex, x)

    override fun setFloatNullable(parameterLabel: String, x: Float?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setFloatNullable(i, x)
        }
    }

    override fun setFloatNullable(parameterIndex: Int, x: Float?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.FLOAT)
        } else {
            this.setFloat(parameterIndex, x)
        }
    }

    override fun setDouble(parameterLabel: String, x: Double) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setDouble(i, x)
        }
    }

    override fun setDouble(parameterIndex: Int, x: Double) = ps.setDouble(parameterIndex, x)

    override fun setDoubleNullable(parameterLabel: String, x: Double?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setDoubleNullable(i, x)
        }
    }

    override fun setDoubleNullable(parameterIndex: Int, x: Double?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.DOUBLE)
        } else {
            this.setDouble(parameterIndex, x)
        }
    }

    override fun setBigDecimal(parameterLabel: String, x: BigDecimal) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setBigDecimal(i, x)
        }
    }

    override fun setBigDecimal(parameterIndex: Int, x: BigDecimal) = ps.setBigDecimal(parameterIndex, x)

    override fun setBigDecimalNullable(parameterLabel: String, x: BigDecimal?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setBigDecimalNullable(i, x)
        }
    }

    override fun setBigDecimalNullable(parameterIndex: Int, x: BigDecimal?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.DECIMAL)
        } else {
            this.setBigDecimal(parameterIndex, x)
        }
    }

    override fun setBoolean(parameterLabel: String, x: Boolean) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setBoolean(i, x)
        }
    }

    override fun setBoolean(parameterIndex: Int, x: Boolean) = ps.setBoolean(parameterIndex, x)

    override fun setBooleanNullable(parameterLabel: String, x: Boolean?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setBooleanNullable(i, x)
        }
    }

    override fun setBooleanNullable(parameterIndex: Int, x: Boolean?) {
        if (x == null) {
            ps.setNull(parameterIndex, Types.BOOLEAN)
        } else {
            this.setBoolean(parameterIndex, x)
        }
    }

    override fun setInstant(parameterLabel: String, x: Instant) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setInstant(i, x)
        }
    }

    override fun setInstant(parameterIndex: Int, x: Instant) =
        this.setTimestamp(parameterIndex, Timestamp.from(x))

    override fun setInstantNullable(parameterLabel: String, x: Instant?) {
        namedParameterSql.params(parameterLabel).forEach { i ->
            setInstantNullable(i, x)
        }
    }

    override fun setInstantNullable(parameterIndex: Int, x: Instant?) {
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
    override fun <R> executeQuery(fn: (rs: IResultSet4k) -> R): R? =
        this.executeListQuery(fn).firstOrNull()

    /**
     * Runs the given [PreparedStatement] and parse the [ResultSet] with the given [fn] If query does not return any
     * result, it returns null.
     *
     * @param fn function used to read the value from query [ResultSet4k]
     * @return a list of type [R] or emptyList if query returns nothing.
     */
    override fun <R> executeListQuery(fn: (rs: IResultSet4k) -> R): List<R> =
        this.executeQuery().use { rs ->
            generateSequence {
                if (!rs.next()) {
                    null
                } else {
                    fn(rs)
                }
            }.toList()
        }

    override fun executeQuery(): IResultSet4k {
        return ResultSet4k(ps.executeQuery())
    }
}
