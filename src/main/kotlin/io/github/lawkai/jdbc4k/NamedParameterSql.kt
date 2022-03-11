package io.github.lawkai.jdbc4k

internal class NamedParameterSql(sql: String) {
    private val parameterRegex = Regex(":\\w+")
    private val parameters = parameterRegex
        .findAll(sql)
        .mapIndexed { i, m -> Pair(i + 1, m.value.substring(1)) }
        .groupBy({ it.second }, { it.first })

    internal val rawSql = sql.replace(parameterRegex, "?")

    fun params(name: String): List<Int> = parameters[name] ?: emptyList()
}
