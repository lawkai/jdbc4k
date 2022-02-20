package com.github.lawkai.jdbc4k.domain

import com.github.lawkai.jdbc4k.DataSource4k
import com.github.lawkai.jdbc4k.executeListQuery
import com.github.lawkai.jdbc4k.executeQuery
import com.github.lawkai.jdbc4k.executeSequenceQuery
import org.slf4j.LoggerFactory

class PersonDAO(private val db: DataSource4k) {
    private val logger = LoggerFactory.getLogger(PersonDAO::class.java)

    suspend fun insert(person: Person) = db.query { conn ->
        logger.debug("inserting $person")
        conn.prepareStatement("INSERT INTO Person(first_name, last_name) VALUES ('${person.firstName}', '${person.lastName}')")
            .executeUpdate()
        person
    }

    suspend fun countPersons(): Int = db.query { conn ->
        logger.debug("Counting persons")
        conn.prepareStatement("SELECT count(*) AS COUNT FROM Person").executeQuery { rs ->
            rs.getInt(1)
        } ?: 0
    }

    suspend fun loadAllPersons(): List<Person> = db.query { conn ->
        logger.debug("select all persons")
        conn.prepareStatement("SELECT * FROM Person").executeListQuery { rs ->
            Person(
                rs.getString("first_name"),
                rs.getString("last_name"),
            )
        }
    }

    suspend fun loadAllPersonsAsSequence(): Sequence<Person> = db.query { conn ->
        logger.debug("select all persons")
        conn.prepareStatement("SELECT * FROM Person").executeSequenceQuery { rs ->
            Person(
                rs.getString("first_name"),
                rs.getString("last_name"),
            )
        }
    }
}
