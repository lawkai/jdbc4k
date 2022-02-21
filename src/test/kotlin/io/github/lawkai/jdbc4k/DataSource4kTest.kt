package io.github.lawkai.jdbc4k

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.lawkai.jdbc4k.dao.PersonDAO
import io.github.lawkai.jdbc4k.domain.Person
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class DataSource4kTest {
    private val personDAO = PersonDAO(db)

    @BeforeEach
    fun setUp() {
        dataSource.connection.use {
            it.prepareStatement("DROP TABLE IF EXISTS person").executeUpdate()
            it.prepareStatement("CREATE TABLE Person(first_name VARCHAR(255), last_name VARCHAR(255))").executeUpdate()
        }
    }

    @Test
    @DisplayName("Queries can be run without transaction.")
    fun testDatabaseQueryWithoutTransaction() = runBlocking {
        personDAO.insert(Person("Donald", "Trump"))
        val count = personDAO.countPersons()
        assertThat(count, equalTo(1))
    }

    @Test
    @DisplayName("Queries inside transaction did not throw Exception should commit the transaction.")
    fun testDatabaseTransactionCommit() = runBlocking {
        db.transaction {
            // everything inside should run in same java.sql.Connection.
            personDAO.insert(Person("Donald", "Trump"))
            personDAO.insert(Person("Joe", "Biden"))
        }
        // this should run in a different java.sql.Connection
        val count = personDAO.countPersons()
        assertThat(count, equalTo(2))
    }

    @Test
    @DisplayName("Exception happened inside the transaction should rollback the transaction.")
    fun testDatabaseTransactionRollback() = runBlocking {
        assertThrows<IllegalStateException> {
            runBlocking {
                db.transaction {
                    personDAO.insert(Person("Donald", "Trump"))
                    error("boom!")
                }
            }
        }
        val count = personDAO.countPersons()
        assertThat(count, equalTo(0))
    }

    @Test
    @DisplayName("Multiple coroutines runs in parallel should be okay.")
    fun testInsertManyRecordConcurrently() = runBlocking {
        val count = 100
        IntRange(1, count).map { i ->
            launch {
                personDAO.insert(Person(i.toString(), i.toString()))
            }
        }.joinAll()
        assertThat(personDAO.countPersons(), equalTo(count))
    }

    @Test
    @DisplayName("Query run in a different coroutine throws an Exception should cause the transaction to rollback.")
    fun testInsertManyButRollback() = runBlocking {
        val count = 100
        assertThrows<IllegalStateException> {
            runBlocking {
                db.transaction {
                    IntRange(1, count).map { i ->
                        if (i == count) {
                            error("bom!")
                        }
                        personDAO.insert(Person(i.toString(), i.toString()))
                    }
                }
            }
        }
        assertThat(personDAO.countPersons(), equalTo(0))
    }

    @Test
    @DisplayName("Nested transaction should commit as a whole.")
    fun testInsertManyWithNestedTransaction() = runBlocking {
        val count = 100
        db.transaction {
            IntRange(1, count).map { i ->
                personDAO.insert(Person(i.toString(), i.toString()))
            }
            db.transaction {
                personDAO.insert(Person("Hillary", "Clinton"))
            }
        }
        assertThat(personDAO.countPersons(), equalTo(count + 1))
    }

    @Test
    @DisplayName("Nested transaction should rollback as a whole.")
    fun testInsertManyWithNestedTransactionButRollbackAtTheEnd() = runBlocking {
        val count = 100
        assertThrows<IllegalStateException> {
            runBlocking {
                db.transaction {
                    IntRange(1, count).map { i ->
                        personDAO.insert(Person(i.toString(), i.toString()))
                    }
                    db.transaction {
                        error("bom!")
                    }
                }
            }
        }
        assertThat(personDAO.countPersons(), equalTo(0))
    }

    @Test
    @DisplayName("Nested transaction with different scope will commit partially.")
    fun testInsertManyWithNestedTransactionOnDifferentScopeButRollbackAtTheEnd() = runBlocking {
        val count = 100
        assertThrows<IllegalStateException> {
            runBlocking {
                db.transaction {
                    IntRange(1, count).map { i ->
                        personDAO.insert(Person("Donald_$i", "Trump"))
                    }

                    // this will be run in a different scope of the transaction.
                    // Thus, each `insert` is actually running on a new connection.
                    launch {
                        IntRange(1, count).map { i ->
                            if (i == count) {
                                error("bom!")
                            }
                            personDAO.insert(Person("Joe_$i", "Biden"))
                        }
                    }
                }
            }
        }
        assertThat(personDAO.countPersons(), equalTo(count * 2 - 1))
    }

    @Test
    @DisplayName("After insert it should be able to query records as List.")
    fun testLoadAllPersonsAsList() = runBlocking {
        db.transaction {
            personDAO.insert(Person("Donald", "Trump"))
            personDAO.insert(Person("Joe", "Biden"))
            personDAO.insert(Person("Hillary", "Clinton"))
        }
        assertThat(personDAO.loadAllPersons().size, equalTo(3))
    }

    @Test
    @DisplayName("After insert it should be able to query records as Sequence.")
    fun testLoadAllPersonsAsSequence() = runBlocking {
        (1..1000).map { i ->
            personDAO.insert(Person("Donald_$i", "Trump"))
            personDAO.insert(Person("Joe_$i", "Biden"))
            personDAO.insert(Person("Hillary_$i", "Clinton"))
        }
        val allDonalds = personDAO.loadAllPersonsAsSequence()
            .filter { it.firstName.startsWith("Donald") }
            .take(30000)
            .onEach {
                println(it)
            }.toList()
        assertThat(allDonalds.size, equalTo(1000))
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
