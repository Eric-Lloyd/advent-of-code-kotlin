package day9

import readInput
import java.util.*

const val MAX_VALUE = 9

fun main() {
    val input = readInput("day9/Day9")
        .map { line -> line.split("").mapNotNull { runCatching { it.toInt() }.getOrNull() } }
    val lowPointSum = findLowPoints(input).sumOf { it + 1 }
    println(lowPointSum)
    val largerBasinsProduct = findBasins(input)
        .sortedBy { it.size }
        .takeLast(3)
        .fold(1) { acc, basins -> acc * basins.size }
    println(largerBasinsProduct)
}

fun findLowPoints(matrix: List<List<Int>>): List<Int> {
    val result = mutableListOf<Int>()
    val n = matrix[0].size
    val m = matrix.size
    for (j in 0 until m) {
        for (i in 0 until n) {
            val curr = matrix[j][i]
            val left = if (i > 0) matrix[j][i - 1] else MAX_VALUE
            val right = if (i < n - 1) matrix[j][i + 1] else MAX_VALUE
            val top = if (j > 0) matrix[j - 1][i] else MAX_VALUE
            val bottom = if (j < m - 1) matrix[j + 1][i] else MAX_VALUE
            if (curr < left && curr < right && curr < top && curr < bottom) {
                result.add(curr)
            }
        }
    }
    return result
}

fun findBasins(matrix: List<List<Int>>): List<Set<BasinSpot>> {
    val basins = mutableListOf<MutableSet<BasinSpot>>()
    val n = matrix[0].size
    val m = matrix.size
    // build initial basins from low points
    for (j in 0 until m) {
        for (i in 0 until n) {
            val curr = matrix[j][i]
            val left = if (i > 0) matrix[j][i - 1] else MAX_VALUE
            val right = if (i < n - 1) matrix[j][i + 1] else MAX_VALUE
            val top = if (j > 0) matrix[j - 1][i] else MAX_VALUE
            val bottom = if (j < m - 1) matrix[j + 1][i] else MAX_VALUE
            if (curr < left && curr < right && curr < top && curr < bottom) {
                val basin = mutableSetOf<BasinSpot>()
                basin.add(BasinSpot(curr, i, j, true))
                if (left != MAX_VALUE) basin.add(BasinSpot(left, i - 1, j, false))
                if (right != MAX_VALUE) basin.add(BasinSpot(right, i + 1, j, false))
                if (top != MAX_VALUE) basin.add(BasinSpot(top, i, j - 1, false))
                if (bottom != MAX_VALUE) basin.add(BasinSpot(bottom, i, j + 1, false))
                basins.add(basin)
            }
        }
    }

    var allHandled = basins.flatten().allHandled()
    // extends basins from not handled spots
    while (!allHandled) {
        for (basin in basins) {
            val spotsToAdd = mutableSetOf<BasinSpot>()
            for (spot in basin) {
                if (!spot.handled) {
                    val (_, i, j, _) = spot
                    val left = if (i > 0) matrix[j][i - 1] else MAX_VALUE
                    val right = if (i < n - 1) matrix[j][i + 1] else MAX_VALUE
                    val top = if (j > 0) matrix[j - 1][i] else MAX_VALUE
                    val bottom = if (j < m - 1) matrix[j + 1][i] else MAX_VALUE
                    if (left != MAX_VALUE) spotsToAdd.add(BasinSpot(left, i - 1, j, false))
                    if (right != MAX_VALUE) spotsToAdd.add(BasinSpot(right, i + 1, j, false))
                    if (top != MAX_VALUE) spotsToAdd.add(BasinSpot(top, i, j - 1, false))
                    if (bottom != MAX_VALUE) spotsToAdd.add(BasinSpot(bottom, i, j + 1, false))
                    spot.handled = true
                }
            }
            basin.addAll(spotsToAdd)
        }
        allHandled = basins.flatten().allHandled()
    }
    return basins
}
data class BasinSpot(val value: Int, val i: Int, val j: Int, var handled: Boolean = false) {
    // ignore 'handled' in hashcode
    override fun hashCode(): Int {
        return Objects.hash(value, i, j)
    }

    // ignore 'handled' in equals
    override fun equals(other: Any?) =
        (other is BasinSpot)
                && value == other.value
                && i == other.i
                && j == other.j
}

fun List<BasinSpot>.allHandled() = this.all { it.handled }