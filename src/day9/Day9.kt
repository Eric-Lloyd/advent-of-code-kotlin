package day9

import readInput
import java.util.Objects

const val MAX_VALUE = 9

fun main() {
    val input = readInput("day9/Day9")
        .map { line -> line.toList().map { it.toString().toInt() } }
    val lowPointSum = findLowPoints(input).sumOf { it.value + 1 }
    println(lowPointSum)
    val largerBasinsProduct = findBasins(input)
        .sortedBy { it.size }
        .takeLast(3)
        .fold(1) { acc, basins -> acc * basins.size }
    println(largerBasinsProduct)
}

fun findLowPoints(matrix: List<List<Int>>): List<BasinPoint> {
    val result = mutableListOf<BasinPoint>()
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
                result.add(BasinPoint(curr, i, j))
            }
        }
    }
    return result
}

fun findBasins(matrix: List<List<Int>>): List<Set<BasinPoint>> {
    val n = matrix[0].size
    val m = matrix.size
    // build initial basins from low points
    val basins = findLowPoints(matrix).map { mutableSetOf(it) }

    var allHandled = false
    // extends basins from not handled points
    while (!allHandled) {
        for (basin in basins) {
            val pointsToAdd = mutableSetOf<BasinPoint>()
            basin.asSequence()
                .filter { point -> !point.handled }
                .forEach { point ->
                    val (_, i, j, _) = point
                    val left = if (i > 0) matrix[j][i - 1] else MAX_VALUE
                    val right = if (i < n - 1) matrix[j][i + 1] else MAX_VALUE
                    val top = if (j > 0) matrix[j - 1][i] else MAX_VALUE
                    val bottom = if (j < m - 1) matrix[j + 1][i] else MAX_VALUE
                    if (left != MAX_VALUE) pointsToAdd.add(BasinPoint(left, i - 1, j))
                    if (right != MAX_VALUE) pointsToAdd.add(BasinPoint(right, i + 1, j))
                    if (top != MAX_VALUE) pointsToAdd.add(BasinPoint(top, i, j - 1))
                    if (bottom != MAX_VALUE) pointsToAdd.add(BasinPoint(bottom, i, j + 1))
                    point.handled = true
                }
            basin.addAll(pointsToAdd)
        }
        allHandled = basins.flatten().all { it.handled }
    }
    return basins
}

data class BasinPoint(val value: Int, val i: Int, val j: Int, var handled: Boolean = false) {
    // ignore 'handled' in hashcode
    override fun hashCode(): Int {
        return Objects.hash(value, i, j)
    }

    // ignore 'handled' in equals
    override fun equals(other: Any?) =
        (other is BasinPoint)
                && value == other.value
                && i == other.i
                && j == other.j
}