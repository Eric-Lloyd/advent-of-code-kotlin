package day11

import readInput

fun main() {
    val input = readInput("day11/Day11")
        .map { line -> line.split("").mapNotNull { runCatching { it.toInt() }.getOrNull() } }

    val counts = countFlashesAfterEachStepUntil(input) { step, flashCount -> step >= 100 && flashCount == 100 }
    val part1 = counts
        .filter { (step, _) -> step <= 100 }
        .map { (_, flashCount) -> flashCount }
        .sum()
    val part2 = counts
        .filter { (_, flashCount) -> flashCount == 100 }
        .firstNotNullOf { (step, _) -> step }
    println(part1)
    println(part2)
}

fun countFlashesAfterEachStepUntil(input: List<List<Int>>, predicate: (Int, Int) -> Boolean): Map<Int, Int> {
    val matrix = initialPointMatrix(input)
    var currStep = 1
    var currFlashCount = 0
    val result = mutableMapOf<Int, Int>()
    while (!predicate(currStep, currFlashCount)) {
        val m = matrix.size
        val n = matrix[0].size
        val zeros = mutableListOf<ZeroPoint>()
        // increment all points and find initial zeros
        for (j in 0 until m) {
            for (i in 0 until n) {
                val currentPoint = matrix[j][i]
                when (currentPoint.value) {
                    9 -> {
                        currentPoint.value = 0
                        zeros.add(ZeroPoint(currentPoint))
                    }
                    else -> currentPoint.value += 1
                }
            }
        }

        // handle zeros
        var allZerosHandled = zeros.isEmpty()
        while (!allZerosHandled) {
            val zerosToAdd = mutableListOf<ZeroPoint>()
            zeros.filter { !it.handled }.forEach {
                val neighbours = it.point.neighbours(matrix)
                neighbours.filter { neighbour -> neighbour.value != 0 }.forEach { neighbour ->
                    when (neighbour.value) {
                        9 -> {
                            neighbour.value = 0
                            zerosToAdd.add(ZeroPoint(neighbour))
                        }
                        else -> neighbour.value += 1
                    }
                }
                it.handled = true
            }
            zeros.addAll(zerosToAdd)
            allZerosHandled = zeros.all { it.handled }
        }
        currFlashCount = zeros.size
        result[currStep] = currFlashCount

        currStep += 1
    }
    return result
}

private fun initialPointMatrix(input: List<List<Int>>): List<List<Point>> {
    val m = input.size
    val n = input[0].size
    val result = mutableListOf<MutableList<Point>>()
    for (j in 0 until m) {
        val row = mutableListOf<Point>()
        for (i in 0 until n) {
            row.add(Point(input[j][i], i, j))
        }
        result.add(row)
    }
    return result
}

data class Point(var value: Int, val i: Int, val j: Int)

fun Point.neighbours(matrix: List<List<Point>>): List<Point> {
    val m = matrix.size
    val n = matrix[0].size
    val (_, i, j) = this
    val top = if (j > 0) matrix[j - 1][i] else null
    val left = if (i > 0) matrix[j][i - 1] else null
    val right = if (i < n - 1) matrix[j][i + 1] else null
    val bottom = if (j < m - 1) matrix[j + 1][i] else null
    val topLeft = if (j > 0 && i > 0) matrix[j - 1][i - 1] else null
    val topRight = if (j > 0 && i < n - 1) matrix[j - 1][i + 1] else null
    val bottomLeft = if (j < m - 1 && i > 0) matrix[j + 1][i - 1] else null
    val bottomRight = if (j < m - 1 && i < n - 1) matrix[j + 1][i + 1] else null
    return listOf(top, left, right, bottom, topLeft, topRight, bottomLeft, bottomRight).mapNotNull { it }
}

class ZeroPoint(val point: Point, var handled: Boolean = false)