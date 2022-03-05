JDBC4K
===

### What is JDBC4k?

It is a small library written in Kotlin (JVM) that allows Coroutines to be run within the same Database transaction.

It is NOT an ORM library like [Hibernate](https://hibernate.org) or [Exposed](https://github.com/JetBrains/Exposed), but
it does provide some useful functions when working with `java.sql.PreparedStatement` and `java.sql.ResultSet`.

The idea of this library is that you as a developer have full control of what SQL to run and how the object should be
mapped without relying on any MAGIC to happen.

```kotlin
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
```

See a working example of this code in [sample](src/test/kotlin/io/github/lawkai/jdbc4k/Sample.kt).
