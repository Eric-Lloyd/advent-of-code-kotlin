package example

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    val input = readInput("example/Day0")
    println(part1(input))
}


