package day7

import readInput
import kotlin.math.abs

fun main() {
    val input = readInput("day7/Day7")[0].split(",").map { it.toInt() }
    val count1 = Part1.findMinimalMoves(input)
    val count2 = Part2.findMinimalMoves(input) { diff -> ((diff * (diff + 1)) / 2).toLong() }
    println(count1)
    println(count2)
}

object Part1 {
    fun findMinimalMoves(positions: List<Int>) =
        Part2.findMinimalMoves(positions) { diff -> diff.toLong() }
}

object Part2 {
    fun findMinimalMoves(positions: List<Int>, cost: (Int) -> Long): Long {
        val sortedPositions = positions.sorted()
        val min = sortedPositions.first()
        val max = sortedPositions.last()
        val counts = mutableListOf<Long>()
        for (i in min..max) {
            var count = 0L
            for (position in sortedPositions) {
                val diff = abs(position - i)
                count += cost(diff)
            }
            counts.add(count)
        }
        return counts.minOrNull() ?: 0L
    }
}