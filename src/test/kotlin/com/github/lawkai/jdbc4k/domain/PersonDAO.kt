package com.github.lawkai.jdbc4k.domain

import com.github.lawkai.jdbc4k.DataSource4k
import org.slf4j.LoggerFactory

class PersonDAO(private val db: DataSource4k) {
    private val logger = LoggerFactory.getLogger(PersonDAO::class.java)

    suspend fun insert(person: Person) = db.query { conn ->
        logger.debug("inserting $person")
        conn.prepareStatement("INSERT INTO Person(first_name, last_name) VALUES ('${person.firstName}', '${person.lastName}')")
            .executeUpdate()
        person
    }

    suspend fun countPersons() = db.query { conn ->
        logger.debug("Counting persons")
        val rs = conn.prepareStatement("SELECT count(*) AS COUNT FROM Person").executeQuery()
        rs.next()
        rs.getInt(1)
    }
}
