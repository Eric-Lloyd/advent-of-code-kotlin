package day1

import readInput

fun main() {
    val input = readInput("day1/Day1").map { it.toInt() }
    println(Part1.countIncrease(input))
    println(Part2.countIncrease(input))
}

object Part1 {
    fun countIncrease(input: List<Int>): Int {
        if (input.isEmpty()) return 0

        var count = 0
        var previous = input[0]
        val tail = input.drop(1)
        for (current in tail) {
            if (current > previous) {
                count += 1
            }
            previous = current
        }
        return count
    }
}

object Part2 {
    fun countIncrease(input: List<Int>): Int =
        Part1.countIncrease(buildTriples(input).map { it.sum()  })

    private fun buildTriples(input: List<Int>): List<Triple> {
        val lastFirst = input.size - 3
        return input
            .filterIndexed { index, _ -> index <= lastFirst }
            .mapIndexed { index, _ ->
                val first = input[index]
                val second = input[index + 1]
                val third = input[index + 2]
                Triple(first, second, third)
            }
    }
}

data class Triple(val first: Int, val second: Int, val third: Int)

fun Triple.sum(): Int = this.first + this.second + this.third