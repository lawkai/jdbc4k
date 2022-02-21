package com.github.lawkai.jdbc4k.dao

import com.github.lawkai.jdbc4k.DataSource4k
import com.github.lawkai.jdbc4k.domain.Person
import com.github.lawkai.jdbc4k.executeListQuery
import com.github.lawkai.jdbc4k.executeQuery
import com.github.lawkai.jdbc4k.executeSequenceQuery
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

    suspend fun loadAllPersonsAsSequence(): Sequence<Person> =
        db.query("SELECT * FROM Person") { ps ->
            logger.debug("select all persons with sequence")
            ps.executeSequenceQuery { rs ->
                Person(
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                )
            }
        }
}
