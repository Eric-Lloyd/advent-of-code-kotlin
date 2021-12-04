package day4

import readInput

fun main() {
    val input = readInput("day4/Day4").filter { it.trimIndent().isNotBlank() }
    val numbers = input[0].split(",").map { it.toInt() }
    val boards = input
        .asSequence()
        .drop(1)
        .map { line -> line.split(" ").filter { it.isNotBlank() }.map { it.toInt() } }
        .map { line -> Row(line.map { Tile(it) }) }
        .chunked(5)
        .map { Board(it) }
        .toList()
    val result1 = Game1(boards, numbers).play()
    val result2 = Game2(boards, numbers).play()
    println(result1)
    println(result2)
}

class Game1(private val boards: List<Board>, private val numbers: List<Int>) {
    fun play(): Int {
        for (number in numbers) {
            boards.mark(number)
            val winningScore = boards.map { it.score(number) }.firstOrNull { it > 0 }
            winningScore?.let { return it }
        }
        return 0
    }
}

class Game2(private val boards: List<Board>, private val numbers: List<Int>) {
    fun play(): Int {
        for (number in numbers) {
            boards.mark(number)
            boards
                .filter { !it.winner }
                .forEach {
                    val score = it.score(number)
                    if (score > 0) {
                        it.winner = true
                        if (boards.all { board -> board.winner }) {
                            return score
                        }
                    }
                }
        }
        return 0
    }
}

// "winner" attribute is only needed for Part2
class Board(private val rows: List<Row>, var winner: Boolean = false) {
    private val sumUnmarked: Int
        get() = rows.sumOf { it.sumUnmarked }

    fun mark(number: Int) {
        rows.forEach { it.mark(number) }
    }

    fun score(number: Int): Int {
        filledRow()?.let { return (sumUnmarked) * number }
        filledColumn()?.let { return (sumUnmarked) * number }
        return 0
    }

    private fun filledRow(): Row? = rows.filled()
    private fun filledColumn(): Row? = rows.toColumns().filled()
}

fun List<Board>.mark(number: Int) = this.forEach { it.mark(number) }

class Row(val tiles: List<Tile>) {
    val sumUnmarked: Int
        get() = tiles.filter { !it.marked }.sumOf { it.value }

    fun mark(number: Int) {
        tiles.filter { it.value == number }.forEach { it.mark() }
    }

    fun isFilled() = tiles.all { it.marked }
}

fun List<Row>.filled() = this.firstOrNull { it.isFilled() }

fun List<Row>.toColumns(): List<Row> {
    val columns = mutableListOf<Row>()
    if (this.isEmpty()) return columns

    for (i in 0 until 5) {
        val tiles = mutableListOf<Tile>()
        for (j in 0 until 5) {
            tiles.add(this[j].tiles[i])
        }
        columns.add(Row(tiles))
    }
    return columns
}

class Tile(val value: Int, var marked: Boolean = false) {
    fun mark() {
        marked = true
    }
}

