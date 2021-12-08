package day8

import readInput

fun main() {
    val input = readInput("day8/Day8")
    val count1 = input
        .flatMap { it.split("|")[1].trimStart().split(" ") }
        .count { it.length in UNIQUE_NUMBER_OF_SEGMENTS }
    println(count1)
    val sum = input.map { it.split(" | ").map { elem -> elem.trimStart().split(" ") } }
        .sumOf { (left, right) -> outputValue(left, right) }
    println(sum)
}

val UNIQUE_NUMBER_OF_SEGMENTS = listOf(2, 3, 4, 7)

fun outputValue(left: List<String>, right: List<String>): Int {
    val one = left.first { it.length == 2 }
    val seven = left.first { it.length == 3 }
    val four = left.first { it.length == 4 }
    val eight = left.first { it.length == 7 }
    val three = three(seven, left.filter { it.length == 5 })
    val top = top(seven, one)
    val bottomLeft = bottomLeft(eight, four, three)
    val topLeft = topLeft(eight, three, bottomLeft)
    val fourMinusSeven = four.filter { !seven.contains(it) }
    val six = six(left.filter { it.length == 6 }, fourMinusSeven, top, bottomLeft, topLeft)
    val bottomRight = bottomRight(six, one)
    val topRight = topRight(one, bottomRight)
    val middle = middle(four, topRight, bottomRight, topLeft)
    val bottom = bottom(eight, top, topRight, bottomRight, bottomLeft, topLeft, middle)
    val two = listOf(top, topRight, middle, bottomLeft, bottom).joinToString("")
    val five = listOf(top, topLeft, middle, bottomRight, bottom).joinToString("")
    val nine = listOf(top, topLeft, middle, topRight, bottomRight, bottom).joinToString("")
    val zero = listOf(top, topLeft, topRight, bottom, bottomLeft, bottomRight).joinToString("")
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
    val result = right
        .map { numbers.entries.first { (key, _) -> it.containsExactly(key) } }
        .map { it.value }.joinToString("")
    return result.toInt()
}

fun top(seven: String, one: String): Char {
    return seven.find { !one.contains(it) }!!
}

fun bottomRight(six: String, one: String): Char {
    return one.first { six.contains(it) }
}

fun topRight(one: String, other: Char): Char {
    return one.find { it != other }!!
}

fun three(seven: String, potentials: List<String>): String {
    return potentials.first { it.containsChars(seven) }
}

fun six(potentials: List<String>, isIn: String, vararg others: Char): String {
    return potentials.first { potential -> others.all { potential.contains(it) } && potential.containsChars(isIn) }
}

fun bottomLeft(eight: String, four: String, three: String): Char {
    return eight.find { !four.contains(it) && !three.contains(it) }!!
}

fun topLeft(eight: String, three: String, bottomLeft: Char): Char {
    return eight.find { !three.contains(it) && it != bottomLeft }!!
}

fun middle(four: String, vararg others: Char): Char {
    return four.find { !others.contains(it) }!!
}

fun bottom(eight: String, vararg others: Char): Char {
    return eight.find { !others.contains(it) }!!
}

fun String.containsChars(other: String) = other.all { this.contains(it) }
fun String.containsExactly(other: String) = this.length == other.length &&
            this.all { contains(it) } &&
            other.all { this.contains(it) }