package day10

import readInput

fun main() {
    val processedLines = readInput("day10/Day10")
        .map { it.toCharArray() }
        .map { processLine(it) }

    // part 1
    val illegalCharacterSum = processedLines
        .filterIsInstance<IllegalLine>()
        .sumOf { ILLEGAL_CHARACTER_POINTS[it.firstIllegal] ?: 0 }
    println(illegalCharacterSum)

    // part 2
    val completionsMedian = processedLines
        .filterIsInstance<IncompleteLine>()
        .map { it.completions.fold(0L) { acc, char -> acc * 5 + COMPLETION_CHARACTER_POINTS[char]!! } }
        .median()
    println(completionsMedian)
}

fun processLine(line: CharArray): ProcessedLine {
    val openingCharacterStack = ArrayDeque<Char>()
    for (char in line) {
        when (char) {
            in OPENING_CHARACTER_PAIRS.keys -> {
                openingCharacterStack.addLast(char)
            }
            in CLOSING_CHARACTER_PAIRS.keys -> {
                val last = openingCharacterStack.removeLast()
                if (CLOSING_CHARACTER_PAIRS[char] != last) {
                    return IllegalLine(char)
                }
            }
            else -> throw IllegalArgumentException("Illegal character in input: ${char}.")
        }
    }
    val lineCompletion = openingCharacterStack.mapNotNull { OPENING_CHARACTER_PAIRS[it] }.reversed()
    return if (lineCompletion.isNotEmpty()) IncompleteLine(lineCompletion) else CompletedLine
}

sealed class ProcessedLine
data class IllegalLine(val firstIllegal: Char) : ProcessedLine()
data class IncompleteLine(val completions: List<Char>) : ProcessedLine()
object CompletedLine : ProcessedLine()

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