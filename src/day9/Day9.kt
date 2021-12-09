package day9

import readInput

fun main() {
    val input = readInput("day9/Day9")
        .map { line -> line.split("").mapNotNull { runCatching { it.toInt() }.getOrNull() } }
    val lowPointSum = findLowPoint(input).sumOf { it + 1 }
    println(lowPointSum)
    val largerBasinsProduct = findBasins(input)
        .sortedBy { it.size }
        .takeLast(3)
        .fold(1) { acc, basins -> acc * basins.size  }
    println(largerBasinsProduct)
}

fun findLowPoint(matrix: List<List<Int>>): List<Int> {
    val result = mutableListOf<Int>()
    val n = matrix[0].size
    val m = matrix.size
    for (j in 0 until m) {
        for (i in 0 until n) {
            val curr = matrix[j][i]
            val left = if (i > 0) matrix[j][i - 1] else Int.MAX_VALUE
            val right = if (i < n - 1) matrix[j][i + 1] else Int.MAX_VALUE
            val top = if (j > 0) matrix[j - 1][i] else Int.MAX_VALUE
            val bottom = if (j < m - 1) matrix[j + 1][i] else Int.MAX_VALUE
            if (curr < left && curr < right && curr < top && curr < bottom) {
                result.add(curr)
            }
        }
    }
    return result
}

fun findBasins(matrix: List<List<Int>>): List<List<BasinSpot>> {
    val basins = mutableListOf<MutableList<BasinSpot>>()
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
                val basin = mutableListOf<BasinSpot>()
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
    while (!allHandled) {
        for (basin in basins) {
            val spotsToAdd = mutableListOf<BasinSpot>()
            for (spot in basin) {
                if (!spot.handled) {
                    val (_, i, j, _) = spot
                    val left = if (i > 0) matrix[j][i - 1] else MAX_VALUE
                    val right = if (i < n - 1) matrix[j][i + 1] else MAX_VALUE
                    val top = if (j > 0) matrix[j - 1][i] else MAX_VALUE
                    val bottom = if (j < m - 1) matrix[j + 1][i] else MAX_VALUE
                    if (left != MAX_VALUE && !basin.contains(i - 1, j) && !spotsToAdd.contains(i - 1, j))
                        spotsToAdd.add(BasinSpot(left, i - 1, j, false))
                    if (right != MAX_VALUE && !basin.contains(i + 1, j) && !spotsToAdd.contains(i + 1, j))
                        spotsToAdd.add(BasinSpot(right, i + 1, j, false))
                    if (top != MAX_VALUE && !basin.contains(i, j - 1) && !spotsToAdd.contains(i, j - 1))
                        spotsToAdd.add(BasinSpot(top, i, j - 1, false))
                    if (bottom != MAX_VALUE && !basin.contains(i, j + 1) && !spotsToAdd.contains(i, j + 1))
                        spotsToAdd.add(BasinSpot(bottom, i, j + 1, false))
                    spot.handled = true
                }
            }
            basin.addAll(spotsToAdd)
        }
        allHandled = basins.flatten().allHandled()
    }
    return basins
}

const val MAX_VALUE = 9

data class BasinSpot(val value: Int, val i: Int, val j: Int, var handled: Boolean = false)

fun List<BasinSpot>.contains(i: Int, j: Int) = this.any { it.i == i && it.j == j }
fun List<BasinSpot>.allHandled() = this.all { it.handled }