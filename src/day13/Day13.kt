package day13

import readInput

fun main() {
    val input = readInput("day13/Day13")
    val emptyLineIndex = input.indexOfFirst { it.isBlank() }

    val coordinates = input.subList(0, emptyLineIndex)
        .map { line -> line.split(",").map { it.toInt() }.let { (x, y) -> Pair(x, y) } }

    val folds = input.subList(emptyLineIndex + 1, input.size)
        .map {
            it
                .replace("fold along ", "")
                .split("=")
                .let { (axis, value) -> Pair(axisFrom(axis), value.toInt()) }
        }

    folds.fold(buildInitialMatrix(coordinates)) { acc, (axis, value) ->
        acc.foldOn(axis, value).also { println(it.count()) }
    }.prettyPrint()
}

fun buildInitialMatrix(coordinates: List<Pair<Int, Int>>): List<List<Boolean>> {
    val result = mutableListOf<MutableList<Boolean>>()
    val (n, m) = coordinates
        .fold(Pair(0, 0)) { (currMaxX, currMaxY), (x, y) -> Pair(maxOf(currMaxX, x), maxOf(currMaxY, y)) }
        .let { (maxX, maxY) -> Pair(maxX + 1, maxY + 1) }

    // initialize boolean matrix to false
    for (j in 0 until m) {
        val row = mutableListOf<Boolean>()
        for (i in 0 until n) {
            row.add(false)
        }
        result.add(row)
    }

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
            val copy = this.take(value).toMutableList()
            var downIndex = value - 1
            var upIndex = value + 1
            while (downIndex >= 0 && upIndex < m) {
                val downRow = this[downIndex]
                val upRow = this[upIndex]
                val merge = downRow.mapIndexed { index, b -> b || upRow[index] }
                copy[downIndex] = merge
                downIndex -= 1
                upIndex += 1
            }
            copy
        }
        Axis.X -> {
            val copy = this.map { it.take(value).toMutableList() }
            var downIndex = value - 1
            var upIndex = value + 1
            while (downIndex >= 0 && upIndex < n) {
                val downRow = this.map { it[downIndex] }
                val upRow = this.map { it[upIndex] }
                val merge = downRow.mapIndexed { index, b -> b || upRow[index] }
                for (j in 0 until m) {
                    copy[j][downIndex] = merge[j]
                }
                downIndex -= 1
                upIndex += 1
            }
            copy
        }
    }
}

fun List<List<Boolean>>.count() = this.flatten().count { it }

fun List<List<Boolean>>.prettyPrint() {
    for (list in this) {
        for (elem in list) {
            if (elem) print("#") else print(".")
        }
        print("\n")
    }
}


enum class Axis {
    X, Y
}

fun axisFrom(char: String) = if (char == "x") Axis.X else Axis.Y