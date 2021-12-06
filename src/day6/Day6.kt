package day6

import readInput

fun main() {
    val initial = readInput("day6/Day6")[0].split(",").map { it.toInt() }
    val count = Fish(initial).countAfter(256)
    println(count)
}


class Fish(initial: List<Int>) {
    private var fishMap: Map<Int, Long> = initial.groupingBy { it }.eachCount() as Map<Int, Long>
    fun countAfter(days: Int): Long {
        for (i in 0 until days) {
            update()
        }
        return fishMap.map { it.value }.fold(0L) { a, b -> a + b }
    }

    private fun update() {
        val updatedFishMap = mutableMapOf<Int, Long>()
        for (fish in 0 until 9) {
            when (fish) {
                6 -> updatedFishMap[fish] = fishMap.value(7) + fishMap.value(0)
                8 -> updatedFishMap[fish] = fishMap.value(0)
                else -> updatedFishMap[fish] = fishMap.value(fish + 1)
            }
        }
        fishMap = updatedFishMap
    }
}

private fun Map<Int, Long>.value(key: Int) = this.getOrDefault(key, 0L)