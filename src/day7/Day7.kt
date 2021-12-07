package day7

import readInput
import kotlin.math.abs

fun main() {
    val input = readInput("day7/Day7")[0].split(",").map { it.toInt() }
    val count1 = findMinimalMoves(input) { n -> n }
    val count1v2 = findMinimalMovesV2(input) { n -> n }
    val count2 = findMinimalMoves(input) { n -> sumConsecutiveNumbers(n) }
    val count2v2 = findMinimalMovesV2(input) { n -> sumConsecutiveNumbers(n) }
    println(count1)
    println(count1v2)
    println(count2)
    println(count2v2)
}

fun sumConsecutiveNumbers(n: Int) = ((n * (n + 1)) / 2) // starting at 1

fun findMinimalMoves(positions: List<Int>, cost: (Int) -> Int): Int {
    val min = positions.minOrZero()
    val max = positions.maxOrZero()
    val counts = mutableListOf<Int>()
    for (index in min..max) {
        var count = 0
        for (position in positions) {
            val diff = abs(position - index)
            count += cost(diff)
        }
        counts.add(count)
    }
    return counts.minOrNull() ?: 0
}

fun findMinimalMovesV2(positions: List<Int>, cost: (Int) -> Int): Int {
    return (positions.minOrZero()..positions.maxOrZero())
        .map { index ->
            positions.map { position -> cost(abs(position - index)) }.sum()
        }.minOrNull() ?: 0
}

fun List<Int>.minOrZero() = this.minOrNull() ?: 0
fun List<Int>.maxOrZero() = this.maxOrNull() ?: 0