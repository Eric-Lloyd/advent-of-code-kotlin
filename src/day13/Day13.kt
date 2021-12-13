package day13

import readInput

fun main() {
    val input = readInput("day13/Day13")
    val emptyLineIndex = input.indexOfFirst { it.isBlank() }

    val coordinates = input.subList(0, emptyLineIndex)
        .map { it.toCoordinate() }

    val folds = input.subList(emptyLineIndex + 1, input.size)
        .map { it.toFold() }

    folds.fold(buildInitialMatrix(coordinates)) { acc, (axis, value) ->
        acc.foldOn(axis, value).also { println(it.count()) }
    }.prettyPrint()
}

private fun String.toCoordinate() = this
    .split(",")
    .map { it.toInt() }
    .let { (x, y) -> Pair(x, y) }

private fun String.toFold() = this
    .replace("fold along ", "")
    .split("=")
    .let { (axis, value) -> Pair(Axis.from(axis), value.toInt()) }

fun buildInitialMatrix(coordinates: List<Pair<Int, Int>>): List<List<Boolean>> {
    val (n, m) = coordinates
        .fold(Pair(0, 0)) { (currMaxX, currMaxY), (x, y) -> Pair(maxOf(currMaxX, x), maxOf(currMaxY, y)) }
        .let { (maxX, maxY) -> Pair(maxX + 1, maxY + 1) }

    // initialize boolean matrix to false
    val result = MutableList(m) { MutableList(n) { false } }

    // fill with coordinates
    for ((x, y) in coordinates) {
        result[y][x] = true
    }
    return result
}

fun List<List<Boolean>>.foldOn(axis: Axis, value: Int): List<List<Boolean>> {
    val n = this[0].size
    val m = this.size
    return when (axis) {
        Axis.Y -> {
            val folded = this.take(value).toMutableList()
            var downIndex = value - 1
            var upIndex = value + 1
            while (downIndex >= 0 && upIndex < m) {
                val downRow = this[downIndex]
                val upRow = this[upIndex]
                folded[downIndex] = downRow.merge(upRow)
                downIndex -= 1
                upIndex += 1
            }
            folded
        }
        Axis.X -> {
            val folded = this.map { it.take(value).toMutableList() }
            var downIndex = value - 1
            var upIndex = value + 1
            while (downIndex >= 0 && upIndex < n) {
                val downRow = this.map { it[downIndex] }
                val upRow = this.map { it[upIndex] }
                val merge = downRow.merge(upRow)
                for (j in 0 until m) {
                    folded[j][downIndex] = merge[j]
                }
                downIndex -= 1
                upIndex += 1
            }
            folded
        }
    }
}

private fun List<Boolean>.merge(other: List<Boolean>) = this.mapIndexed { index, b -> b || other[index] }

private fun List<List<Boolean>>.count() = this.flatten().count { it }

fun List<List<Boolean>>.prettyPrint() = this.forEach { line ->
    line.forEach { elem -> if (elem) print("#") else print(" ") }.also { print("\n") }
}

enum class Axis {
    X, Y;

    companion object {
        fun from(char: String) = if (char == "x") X else Y
    }
}