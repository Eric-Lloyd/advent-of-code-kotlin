package day14

import java.io.File

fun main() {
    val input = File("src", "day14/Day14.txt").readLines()

    val template = input[0]
    val rules = input
        .drop(2)
        .map { it.split(" -> ").let { (window, target) -> Rule(window, target) } }

    val matcher = Matcher(rules)
    val polymers = buildPolymers(template, matcher, 40)
    val sortedCounts = letterCounts(template[0], polymers)
        .toList()
        .sortedBy { it.second }
        .map { it.second }

    val result = sortedCounts.last() - sortedCounts.first()
    println(result)
}

class Rule(val window: String, private val target: String) {
    fun generate() = listOf(window[0] + target, target + window[1])
}

class Matcher(rules: List<Rule>) {
    private val map = rules.groupBy { it.window }
    fun match(window: String) = map[window]!![0]
}

fun buildPolymers(template: String, matcher: Matcher, steps: Int): Map<String, Long> {
    val initialWindows = template
        .windowed(2)
        .groupingBy { it }
        .eachCount() as Map<String, Long>

    return (0 until steps).fold(initialWindows) { currWindows, _ ->
        val nextWindows = mutableMapOf<String, Long>()
        currWindows.forEach { (window, count) ->
            matcher.match(window).generate().forEach {
                nextWindows[it] = (nextWindows[it] ?: 0) + count
            }
        }
        nextWindows
    }
}

fun letterCounts(first: Char, polymers: Map<String, Long>): Map<Char, Long> {
    val letterCounts = mapOf(first to 1L).toMutableMap()
    polymers.forEach { (window, count) ->
        letterCounts[window[1]] = (letterCounts[window[1]] ?: 0) + count
    }
    return letterCounts
}