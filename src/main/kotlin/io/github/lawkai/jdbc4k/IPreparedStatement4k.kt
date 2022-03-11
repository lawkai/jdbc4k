package io.github.lawkai.jdbc4k

import java.math.BigDecimal
import java.sql.PreparedStatement
import java.time.Instant

interface IPreparedStatement4k : PreparedStatement {
    fun setByte(parameterLabel: String, x: Byte)
    override fun setByte(parameterIndex: Int, x: Byte)
    fun setByteNullable(parameterLabel: String, x: Byte?)
    fun setByteNullable(parameterIndex: Int, x: Byte?)
    fun setBytes(parameterLabel: String, x: ByteArray)
    override fun setBytes(parameterIndex: Int, x: ByteArray)
    fun setBytesNullable(parameterLabel: String, x: ByteArray?)
    fun setBytesNullable(parameterIndex: Int, x: ByteArray?)
    fun setString(parameterLabel: String, x: String)
    override fun setString(parameterIndex: Int, x: String)
    fun setStringNullable(parameterLabel: String, x: String?)
    fun setStringNullable(parameterIndex: Int, x: String?)
    fun setShort(parameterLabel: String, x: Short)
    fun setShortNullable(parameterLabel: String, x: Short?)
    fun setShortNullable(parameterIndex: Int, x: Short?)
    fun setInt(parameterLabel: String, x: Int)
    fun setIntNullable(parameterLabel: String, x: Int?)
    fun setIntNullable(parameterIndex: Int, x: Int?)
    fun setLong(parameterLabel: String, x: Long)
    fun setLongNullable(parameterLabel: String, x: Long?)
    fun setLongNullable(parameterIndex: Int, x: Long?)
    fun setFloat(parameterLabel: String, x: Float)
    fun setFloatNullable(parameterLabel: String, x: Float?)
    fun setFloatNullable(parameterIndex: Int, x: Float?)
    fun setDouble(parameterLabel: String, x: Double)
    fun setDoubleNullable(parameterLabel: String, x: Double?)
    fun setDoubleNullable(parameterIndex: Int, x: Double?)
    fun setBigDecimal(parameterLabel: String, x: BigDecimal)
    fun setBigDecimalNullable(parameterLabel: String, x: BigDecimal?)
    fun setBigDecimalNullable(parameterIndex: Int, x: BigDecimal?)
    fun setBoolean(parameterLabel: String, x: Boolean)
    fun setBooleanNullable(parameterLabel: String, x: Boolean?)
    fun setBooleanNullable(parameterIndex: Int, x: Boolean?)
    fun setInstant(parameterLabel: String, x: Instant)
    fun setInstantNullable(parameterLabel: String, x: Instant?)
    fun setInstant(parameterIndex: Int, x: Instant)
    fun setInstantNullable(parameterIndex: Int, x: Instant?)
    fun <R> executeQuery(fn: (rs: IResultSet4k) -> R): R?
    fun <R> executeListQuery(fn: (rs: IResultSet4k) -> R): List<R>
    override fun executeQuery(): IResultSet4k
}
