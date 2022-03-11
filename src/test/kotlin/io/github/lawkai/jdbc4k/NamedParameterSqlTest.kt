package io.github.lawkai.jdbc4k

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class NamedParameterSqlTest {
    private val namedParamSql = NamedParameterSql("SELECT * FROM SOMETHING WHERE id=:id AND something=:id")

    @Test
    fun testStripeParameter() {
        assertThat(namedParamSql.rawSql, equalTo("SELECT * FROM SOMETHING WHERE id=? AND something=?"))
    }
}
