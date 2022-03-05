package io.github.lawkai.jdbc4k

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import javax.sql.DataSource
import kotlin.random.Random

data class Order(
    val orderNumber: String,
    val totalAmount: BigDecimal,
    val orderDateTime: Instant,
    val id: Long? = null,
    val createDateTime: Instant? = null,
    val updateDateTime: Instant? = null,
    val version: Int? = null,
)

class Sample(private val db: DataSource4k) {
    private val logger = LoggerFactory.getLogger(Sample::class.java)

    private suspend fun createOrder(order: Order): Int =
        db.query("INSERT INTO MY_ORDER(order_number, total_amount, order_datetime) VALUES (?, ?, ?)") { ps ->
            logger.debug("creating Order ${order.orderNumber} with ${order.totalAmount}")
            ps.setString(1, order.orderNumber)
            ps.setBigDecimal(2, order.totalAmount)
            ps.setInstant(3, order.orderDateTime)
            ps.executeUpdate()
        }

    fun selectOrders(): Flow<Order> = db.flowQuery("SELECT * FROM MY_ORDER") { rs ->
        Order(
            orderNumber = rs.getString("order_number"),
            totalAmount = rs.getBigDecimal("total_amount"),
            orderDateTime = rs.getInstant("order_datetime"),
            // db managed columns
            id = rs.getLong("id"),
            createDateTime = rs.getInstant("create_datetime"),
            updateDateTime = rs.getInstant("update_datetime"),
            version = rs.getInt("version"),
        )
    }

    suspend fun generateOrders() {
        // all the insert will be running with the same connection
        val amount = BigDecimal(Random.nextDouble(0.1, 100.00))
        val orderNumber = UUID.randomUUID().toString()
        createOrder(Order(orderNumber = orderNumber, totalAmount = amount, orderDateTime = Instant.now()))
    }
}

private val dataSource: DataSource = HikariDataSource(
    HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://localhost:13306/testdb"
        username = "user"
        password = "password"
        maximumPoolSize = 10
        isAutoCommit = true
        poolName = "testDb"
        transactionIsolation = "TRANSACTION_READ_COMMITTED"
    },
)

private val logger = LoggerFactory.getLogger("io.github.lawkai.jdbc4k.Sample")

/**
 * Please check out how to start the given mariadb from docker with the given `docker-compose.yml` file in
 * `test/resources` directory
 */
fun main() = runBlocking {
    val db = DataSource4k(dataSource, 10)
    val sample = Sample(db)

    val job = launch {
        db.transaction {
            launch(Dispatchers.Unconfined) {
                while (isActive) {
                    sample.generateOrders()
                    delay(100)
                }
            }
            launch(Dispatchers.Unconfined) {
                while (isActive) {
                    sample.generateOrders()
                    delay(100)
                }
            }
        }
    }

    delay(1000)
    logger.info("Cancelling job")
    job.cancelAndJoin()
    logger.info("Job Cancelled")

    val orders = sample.selectOrders()
    logger.info("Number of orders created: ${orders.count()}") // should return 0
}
