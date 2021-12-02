package day2

import readInput

fun main() {
    val commands = readInput("day2/Day2")
        .map {
            val line = it.split(" ")
            Command(Type.valueOf(line[0].uppercase()), line[1].toInt())
        }
    val horizontalPosition1 = Part1.horizontalPosition(commands)
    val depth1 = Part1.depth(commands)
    println(horizontalPosition1 * depth1)

    val (_, horizontalPosition2, depth2) = Part2.attributes(commands)
    println(horizontalPosition2 * depth2)
}

object Part1 {
    fun horizontalPosition(commands: List<Command>) =
        commands
            .filter { it.type == Type.FORWARD }
            .map { it.amount }
            .sum()

    fun depth(commands: List<Command>) =
        commands
            .filter { it.type == Type.DOWN || it.type == Type.UP }
            .map { if (it.type == Type.DOWN) it.amount else -it.amount }
            .sum()
}

object Part2 {
    fun attributes(commands: List<Command>): Attributes {
        return commands
            .fold(
                Attributes(0, 0, 0),
                { acc, command -> operation(acc, command) }
            )
    }

    private fun operation(attributes: Attributes, command: Command): Attributes {
        val (aim, horizontalPosition, depth) = attributes
        return when (command.type) {
            Type.FORWARD -> Attributes(aim, horizontalPosition + command.amount, depth + (aim * command.amount))
            Type.UP -> Attributes(aim - command.amount, horizontalPosition, depth)
            Type.DOWN -> Attributes(aim + command.amount, horizontalPosition, depth)
        }
    }
}

enum class Type {
    FORWARD, UP, DOWN
}

data class Command(val type: Type, val amount: Int)

data class Attributes(val aim: Int, val horizontalPosition: Int, val depth: Int)