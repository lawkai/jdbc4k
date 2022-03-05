package io.github.lawkai.jdbc4k.dao

import io.github.lawkai.jdbc4k.DataSource4k
import io.github.lawkai.jdbc4k.domain.Person
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory

class PersonDAO(private val db: DataSource4k) {
    private val logger = LoggerFactory.getLogger(PersonDAO::class.java)

    suspend fun insert(person: Person) =
        db.query("INSERT INTO Person(first_name, last_name) VALUES (?, ?)") { ps ->
            logger.debug("inserting $person")
            ps.apply {
                setString(1, person.firstName)
                setString(2, person.lastName)
            }.executeUpdate()
            person
        }

    suspend fun countPersons(): Int =
        db.query("SELECT count(*) AS COUNT FROM Person") { ps ->
            logger.debug("Counting persons")
            ps.executeQuery { rs ->
                rs.getInt("COUNT")
            } ?: 0
        }

    suspend fun loadAllPersons(): List<Person> =
        db.query("SELECT * FROM Person") { ps ->
            logger.debug("select all persons")
            ps.executeListQuery { rs ->
                Person(
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                )
            }
        }

    fun loadAllPersonsAsSequence(): Flow<Person> =
        db.flowQuery("SELECT * FROM Person") { rs ->
            logger.debug("select all persons with sequence")
            Person(
                rs.getString("first_name"),
                rs.getString("last_name"),
            )
        }
}
