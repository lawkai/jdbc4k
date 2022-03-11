package io.github.lawkai.jdbc4k

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.junit5.JUnit5Asserter.assertNull

internal class PreparedResult4kTest {
    @BeforeEach
    fun setUp() {
        dataSource.connection.use {
            it.prepareStatement("DROP TABLE IF EXISTS NULL_TABLE").executeUpdate()
            it.prepareStatement(
                """
                    CREATE TABLE NULL_TABLE(
                        uuid VARCHAR(255) NOT NULL,
                        nullable_string VARCHAR(255) DEFAULT NULL,
                        nullable_short TINYINT DEFAULT NULL,
                        nullable_int INT DEFAULT NULL,
                        nullable_long BIGINT DEFAULT NULL,
                        nullable_float FLOAT DEFAULT NULL,
                        nullable_double DOUBLE DEFAULT NULL,
                        nullable_big_decimal DECIMAL(16,8) DEFAULT NULL,
                        nullable_boolean BOOLEAN DEFAULT NULL,
                        nullable_timestamp TIMESTAMP DEFAULT NULL,
                        nullable_byte BIT(1) DEFAULT NULL,
                        nullable_bytes BLOB DEFAULT NULL
                    );
                """.trimIndent(),
            ).executeUpdate()
        }
    }

    private suspend fun insert(
        uuid: String,
        nullableString: String? = null,
        nullableShort: Short? = null,
        nullableInt: Int? = null,
        nullableLong: Long? = null,
        nullableFloat: Float? = null,
        nullableDouble: Double? = null,
        nullableBigDecimal: BigDecimal? = null,
        nullableBoolean: Boolean? = null,
        nullableTimestamp: Instant? = null,
        nullableByte: Byte? = null,
        nullableByteArray: ByteArray? = null
    ) {
        db.query(
            """
            INSERT INTO NULL_TABLE (
                uuid,
                nullable_string,
                nullable_short,
                nullable_int,
                nullable_long,
                nullable_float,
                nullable_double,
                nullable_big_decimal,
                nullable_boolean,
                nullable_timestamp,
                nullable_byte,
                nullable_bytes
            ) VALUES (
                :uuid,
                :nullableString,
                :nullableShort,
                :nullableInt,
                :nullableLong,
                :nullableFloat,
                :nullableDouble,
                :nullableBigDecimal,
                :nullableBoolean,
                :nullableInstant,
                :nullableByte,
                :nullableBytes
            )
            """.trimIndent(),
        ) { ps ->
            ps.setString("uuid", uuid)
            ps.setStringNullable("nullableString", nullableString)
            ps.setShortNullable("nullableShort", nullableShort)
            ps.setIntNullable("nullableInt", nullableInt)
            ps.setLongNullable("nullableLong", nullableLong)
            ps.setFloatNullable("nullableFloat", nullableFloat)
            ps.setDoubleNullable("nullableDouble", nullableDouble)
            ps.setBigDecimalNullable("nullableBigDecimal", nullableBigDecimal)
            ps.setBooleanNullable("nullableBoolean", nullableBoolean)
            ps.setInstantNullable("nullableInstant", nullableTimestamp)
            ps.setByteNullable("nullableByte", nullableByte)
            ps.setBytesNullable("nullableBytes", nullableByteArray)
            ps.executeUpdate()
        }
    }

    @Test
    @DisplayName("PreparedStatement should support null value on nullable columns")
    fun testRetrieveNullColumnValues() = runBlocking {
        insert("1")
        val rs = db.query("SELECT * FROM NULL_TABLE WHERE uuid='1'") { ps ->
            ps.executeQuery {
                mapOf<String, Any?>(
                    "uuid" to it.getString("uuid"),
                    "nullable_string" to it.getStringNullable("nullable_string"),
                    "nullable_short" to it.getShortNullable("nullable_short"),
                    "nullable_int" to it.getIntNullable("nullable_int"),
                    "nullable_long" to it.getLongNullable("nullable_long"),
                    "nullable_float" to it.getFloatNullable("nullable_float"),
                    "nullable_double" to it.getDoubleNullable("nullable_double"),
                    "nullable_big_decimal" to it.getBigDecimalNullable("nullable_big_decimal"),
                    "nullable_boolean" to it.getBooleanNullable("nullable_boolean"),
                    "nullable_timestamp" to it.getInstantNullable("nullable_timestamp"),
                    "nullable_byte" to it.getByteNullable("nullable_byte"),
                    "nullable_bytes" to it.getBytesNullable("nullable_bytes"),
                )
            }
        }
        assertNotNull(rs)
        assertEquals("1", rs["uuid"])
        assertNull("nullable_string is not null", rs["nullable_string"])
        assertNull("nullable_short is not null", rs["nullable_short"])
        assertNull("nullable_int is not null", rs["nullable_int"])
        assertNull("nullable_long is not null", rs["nullable_long"])
        assertNull("nullable_float is not null", rs["nullable_float"])
        assertNull("nullable_double is not null", rs["nullable_double"])
        assertNull("nullable_big_decimal is not null", rs["nullable_big_decimal"])
        assertNull("nullable_boolean is not null", rs["nullable_boolean"])
        assertNull("nullable_timestamp is not null", rs["nullable_timestamp"])
        assertNull("nullable_byte is not null", rs["nullable_byte"])
        assertNull("nullable_bytes is not null", rs["nullable_bytes"])
    }

    @Test
    @DisplayName("PreparedStatement should support non-null value on nullable columns")
    fun testRetrieveColumnValuesOnNullableColumns() = runBlocking {
        insert(
            "1", "someString", 1, Int.MAX_VALUE, Long.MAX_VALUE,
            Float.MAX_VALUE, Double.MAX_VALUE, BigDecimal.ONE, true, Instant.EPOCH,
            1.toByte(), ByteArray(1),
        )
        val rs = db.query("SELECT * FROM NULL_TABLE WHERE uuid = '1'") { ps ->
            ps.executeQuery {
                mapOf<String, Any?>(
                    "uuid" to it.getString("uuid"),
                    "nullable_string" to it.getStringNullable("nullable_string"),
                    "nullable_short" to it.getShortNullable("nullable_short"),
                    "nullable_int" to it.getIntNullable("nullable_int"),
                    "nullable_long" to it.getLongNullable("nullable_long"),
                    "nullable_float" to it.getFloatNullable("nullable_float"),
                    "nullable_double" to it.getDoubleNullable("nullable_double"),
                    "nullable_big_decimal" to it.getBigDecimalNullable("nullable_big_decimal"),
                    "nullable_boolean" to it.getBooleanNullable("nullable_boolean"),
                    "nullable_timestamp" to it.getInstantNullable("nullable_timestamp"),
                    "nullable_byte" to it.getByteNullable("nullable_byte"),
                    "nullable_bytes" to it.getBytesNullable("nullable_bytes"),
                )
            }
        }
        assertNotNull(rs)
        assertEquals("1", rs["uuid"])
        assertEquals("someString", rs["nullable_string"])
        assertEquals(1.toShort(), rs["nullable_short"])
        assertEquals(Int.MAX_VALUE, rs["nullable_int"])
        assertEquals(Long.MAX_VALUE, rs["nullable_long"])
        assertEquals(Float.MAX_VALUE, rs["nullable_float"])
        assertEquals(Double.MAX_VALUE, rs["nullable_double"])
        assertEquals(0, BigDecimal.ONE.compareTo(rs["nullable_big_decimal"] as BigDecimal))
        assertEquals(true, rs["nullable_boolean"])
        assertEquals(Instant.EPOCH, rs["nullable_timestamp"])
        assertEquals(1.toByte(), rs["nullable_byte"])
        assertEquals(ByteArray(1).size, (rs["nullable_bytes"] as ByteArray).size)
    }

    companion object {
        val dataSource = HikariDataSource(
            HikariConfig().apply {
                driverClassName = "org.hsqldb.jdbc.JDBCDriver"
                jdbcUrl = "jdbc:hsqldb:mem:coroutine_test"
                maximumPoolSize = 10
                isAutoCommit = true
                poolName = "testDb"
            },
        )
        val db = DataSource4k(dataSource, 10)
    }
}
