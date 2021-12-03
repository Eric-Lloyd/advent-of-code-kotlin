package day3

import readInput

fun main() {
    val input = readInput("day3/Day3")
    val rows = input.map { it.toList() }
    val (gammaRate, epsilonRate) = Part1.getRates(rows)
    println(gammaRate * epsilonRate)

    val (oxygenGenerator, co2Scrubber) = Part2.getRates(rows)
    println(oxygenGenerator * co2Scrubber)
}

object Part1 {
    fun getRates(rows: List<List<Char>>): Pair<Int, Int> {
        val gammaRateAsList = getColumns(rows).map { it.mostFrequentChar() }
        val epsilonRateAsList = gammaRateAsList.map { if (it == '0') '1' else '0' }
        val gammaRate = toDecimal(gammaRateAsList.asString())
        val epsilonRate = toDecimal(epsilonRateAsList.asString())
        return Pair(gammaRate, epsilonRate)
    }
}

object Part2 {
    fun getRates(rows: List<List<Char>>): Pair<Int, Int> {
        val columns = getColumns(rows)
        val oxygenGenerator = getRate(rows, columns) { chars -> mostFrequentBit(chars) }
        val co2Scrubber = getRate(rows, columns) { chars -> lessFrequentBit(chars) }
        return Pair(oxygenGenerator, co2Scrubber)
    }

    private fun getRate(
        rows: List<List<Char>>,
        columns: List<List<Char>>,
        operation: (chars: List<Char>) -> Char
    ): Int {
        var remainingRows = rows
        for (i in columns.indices) {
            var remainingColumns = getColumns(remainingRows)
            val column = remainingColumns[i]
            val bit = operation(column)
            remainingRows = remainingRows.hasAt(bit, i)
            if (remainingRows.size == 1) {
                return toDecimal(remainingRows[0].asString())
            }
        }
        return 0
    }
}

private fun getColumns(rows: List<List<Char>>): List<List<Char>> {
    val columns = mutableListOf<List<Char>>()
    if (rows.isEmpty()) return columns

    val numberOfColumns = rows[0].size
    val numberOfRows = rows.size
    for (i in 0 until numberOfColumns) {
        val column = mutableListOf<Char>()
        for (j in 0 until numberOfRows) {
            column.add(rows[j][i])
        }
        columns.add(column)
    }
    return columns
}

fun List<Char>.mostFrequentChar() =
    this.groupBy { it }.maxByOrNull { it.value.size }?.key ?: '0'

fun mostFrequentBit(chars: List<Char>) =
    chars.bitFrom { a, b -> a > b }

fun lessFrequentBit(chars: List<Char>) =
    chars.bitFrom { a, b -> a <= b }

private fun List<Char>.bitFrom(comparator: (o1: Int, o2: Int) -> Boolean) : Char {
    val count = this.groupingBy { it }.eachCount()
    val count0 = count.getOrDefault('0', 0)
    val count1 = count.getOrDefault('1', 0)
    return if (comparator(count0, count1)) '0' else '1'
}

fun List<List<Char>>.hasAt(elem: Char, index: Int) =
    this.filter { it[index] == elem }

fun List<Char>.asString() =
    this.joinToString("")

fun toDecimal(binaryNumber: String) = Integer.parseInt(binaryNumber, 2)