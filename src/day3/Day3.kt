package day3

import java.io.File

fun main() {
    val rows = File("src", "day3/Day3.txt").readLines().map { it.toList() }

    val (gammaRate, epsilonRate) = Part1.getRates(rows)
    println(gammaRate * epsilonRate)

    val (oxygenGenerator, co2Scrubber) = Part2.getRates(rows)
    println(oxygenGenerator * co2Scrubber)
}

object Part1 {
    fun getRates(rows: List<List<Char>>): Pair<Int, Int> {
        val gammaRateAsList = rows.getColumns().map { it.mostFrequentChar() }
        val epsilonRateAsList = gammaRateAsList.map { if (it == '0') '1' else '0' }
        val gammaRate = gammaRateAsList.toDecimal()
        val epsilonRate = epsilonRateAsList.toDecimal()
        return Pair(gammaRate, epsilonRate)
    }
}

object Part2 {
    fun getRates(rows: List<List<Char>>): Pair<Int, Int> {
        val columns = rows.getColumns()
        val oxygenGenerator = getRate(rows, columns) { it.mostFrequentBit() }
        val co2Scrubber = getRate(rows, columns) { it.lessFrequentBit() }
        return Pair(oxygenGenerator, co2Scrubber)
    }

    private fun getRate(
        rows: List<List<Char>>,
        columns: List<List<Char>>,
        operation: (chars: List<Char>) -> Char
    ): Int {
        var remainingRows = rows
        for (i in columns.indices) {
            var remainingColumns = remainingRows.getColumns()
            val column = remainingColumns[i]
            val bit = operation(column)
            remainingRows = remainingRows.hasAt(bit, i)
            if (remainingRows.size == 1) {
                return remainingRows[0].toDecimal()
            }
        }
        return 0
    }
}

fun List<List<Char>>.getColumns(): List<List<Char>> {
    val columns = mutableListOf<List<Char>>()
    if (this.isEmpty()) return columns.toList()
    val numberOfColumns = this[0].size
    val numberOfRows = this.size
    for (i in 0 until numberOfColumns) {
        val column = mutableListOf<Char>()
        for (j in 0 until numberOfRows) {
            column.add(this[j][i])
        }
        columns.add(column)
    }
    return columns.toList()
}

fun List<Char>.mostFrequentChar() = groupBy { it }.maxByOrNull { it.value.size }?.key ?: '0'

fun List<Char>.mostFrequentBit() = bitFrom { a, b -> a > b }

fun List<Char>.lessFrequentBit() = bitFrom { a, b -> a <= b }

private fun List<Char>.bitFrom(comparator: (first: Int, second: Int) -> Boolean): Char {
    val count = this.groupingBy { it }.eachCount()
    val eval = comparator(count.getOrDefault('0', 0), count.getOrDefault('1', 0))
    return if (eval) '0' else '1'
}

fun List<List<Char>>.hasAt(elem: Char, index: Int) = filter { it[index] == elem }

fun List<Char>.toDecimal() = Integer.parseInt(this.joinToString(""), 2)