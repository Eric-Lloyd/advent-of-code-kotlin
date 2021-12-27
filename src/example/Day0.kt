package example

import java.io.File

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    val input = File("src", "${"example/Day0"}.txt").readLines()
    println(part1(input))
}


