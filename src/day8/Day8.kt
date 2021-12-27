package day8

import java.io.File

fun main() {
    val input = File("src", "day8/Day8.txt").readLines()
    val count1 = input
        .flatMap { it.split(" | ")[1].split(" ") }
        .count { it.length in UNIQUE_NUMBER_OF_SEGMENTS }
    println(count1)
    val sum = input.map { it.split(" | ").map { elem -> elem.split(" ") } }
        .sumOf { (left, right) -> outputValue(left, right) }
    println(sum)
}

val UNIQUE_NUMBER_OF_SEGMENTS = listOf(2, 3, 4, 7)

fun outputValue(left: List<String>, right: List<String>): Int {
    val one = left.first { it.length == 2 }
    val seven = left.first { it.length == 3 }
    val four = left.first { it.length == 4 }
    val eight = left.first { it.length == 7 }

    val lengthSix = left.filter { it.length == 6 } // contains 0, 6 and 8
    val nine = lengthSix.first { it.containsChars(four) }
    val zero = lengthSix.filter { it != nine }.first { it.containsChars(one) }
    val six = lengthSix.first { it != nine && it != zero }

    val lengthFive = left.filter { it.length == 5 } // contains 2, 3 and 5
    val three = lengthFive.first { it.containsChars(seven) }
    val five = lengthFive.first { six.containsChars(it) }
    val two = lengthFive.first { it != three && it != five }

    val numbers = mapOf(
        Pair(zero, 0),
        Pair(one, 1),
        Pair(two, 2),
        Pair(three, 3),
        Pair(four, 4),
        Pair(five, 5),
        Pair(six, 6),
        Pair(seven, 7),
        Pair(eight, 8),
        Pair(nine, 9),
    )

    return right
        .map { numbers.entries.first { (key, _) -> it.containsExactlyInAnyOrder(key) } }
        .map { it.value }.joinToString("").toInt()
}


fun String.containsChars(other: String) = other.all { contains(it) }

fun String.containsExactlyInAnyOrder(other: String) =
    length == other.length &&
            all { contains(it) } &&
            other.all { contains(it) }