package day1

import java.io.File

fun main() {
    val input = File("src", "day1/Day1.txt").readLines().map { it.toInt() }

    // part 1
    println(countIncreases(input))
    // part 2
    println(countIncreases(buildSums(input)))
}

fun countIncreases(input: List<Int>) =
    input.windowed(2).count { (first, second) -> second > first }

fun buildSums(input: List<Int>): List<Int> {
    val lastFirst = input.size - 3
    return input
        .filterIndexed { index, _ -> index <= lastFirst }
        .mapIndexed { index, _ -> input[index] + input[index + 1] + input[index + 2] }
}