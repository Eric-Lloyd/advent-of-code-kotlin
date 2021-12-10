package day10

import readInput

fun main() {
    val input = readInput("day10/Day10").map { it.toCharArray() }
    val illegalCharacterSum = firstIllegalCharacters(input)
        .sumOf { ILLEGAL_CHARACTER_POINTS[it] ?: 0 }
    println(illegalCharacterSum)
    val completionsMedian = incompleteLinesCompletion(input)
        .map { it.fold(0L) { acc, char -> acc * 5 + COMPLETION_CHARACTER_POINTS[char]!! } }
        .median()
    println(completionsMedian)
}

fun firstIllegalCharacters(lines: List<CharArray>): List<Char> {
    val result = mutableListOf<Char>()
    for (line in lines) {
        val openingCharacterStack = ArrayDeque<Char>()
        for (char in line) {
            when (char) {
                in OPENING_CHARACTER_PAIRS.keys -> {
                    openingCharacterStack.addLast(char)
                }
                in CLOSING_CHARACTER_PAIRS.keys -> {
                    val last = openingCharacterStack.removeLast()
                    if (CLOSING_CHARACTER_PAIRS[char] != last) {
                        result.add(char)
                        break
                    }
                }
                else -> throw IllegalArgumentException("Illegal character in input: ${char}.")
            }
        }
    }
    return result
}

fun incompleteLinesCompletion(lines: List<CharArray>): List<List<Char>> {
    val result = mutableListOf<List<Char>>()
    for (line in lines) {
        val openingCharacterStack = ArrayDeque<Char>()
        for (char in line) {
            when (char) {
                in OPENING_CHARACTER_PAIRS.keys -> {
                    openingCharacterStack.addLast(char)
                }
                in CLOSING_CHARACTER_PAIRS.keys -> {
                    val last = openingCharacterStack.removeLast()
                    if (CLOSING_CHARACTER_PAIRS[char] != last) {
                        openingCharacterStack.clear()
                        break
                    }
                }
                else -> throw IllegalArgumentException("Illegal character in input: ${char}.")
            }
        }
        val lineCompletion = openingCharacterStack.mapNotNull { OPENING_CHARACTER_PAIRS[it] }.reversed()
        if (lineCompletion.isNotEmpty()) result.add(lineCompletion)
    }
    return result
}

fun List<Long>.median() = this.sorted().let {
    if (it.size % 2 == 0)
        (it[it.size / 2] + it[(it.size - 1) / 2]) / 2
    else
        it[it.size / 2]
}

val ILLEGAL_CHARACTER_POINTS = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

val COMPLETION_CHARACTER_POINTS = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4,
)

val OPENING_CHARACTER_PAIRS = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

val CLOSING_CHARACTER_PAIRS = mapOf(
    ')' to '(',
    ']' to '[',
    '}' to '{',
    '>' to '<',
)