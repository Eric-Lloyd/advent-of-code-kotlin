package day6

import readInput

fun main() {
    val initial = readInput("day6/Day6")[0].split(",").map { it.toInt() }
    val count1 = Fish(initial).countAfter(80)
    val count2 = Fish(initial).countAfter(256)
    println(count1)
    println(count2)
}


class Fish(initial: List<Int>) {
    private var fishMap: Map<Int, Long> = initial.groupingBy { it }.eachCount() as Map<Int, Long>

    fun countAfter(days: Int): Long {
        for (i in 0 until days) {
            update()
        }
        return fishMap.map { it.value }.sum()
    }

    private fun update() {
        val updatedFishMap = mutableMapOf<Int, Long>()
        for (timer in 0 until 9) {
            when (timer) {
                6 -> updatedFishMap[timer] = fishMap.value(7) + fishMap.value(0)
                8 -> updatedFishMap[timer] = fishMap.value(0)
                else -> updatedFishMap[timer] = fishMap.value(timer + 1)
            }
        }
        fishMap = updatedFishMap
    }
}

private fun Map<Int, Long>.value(key: Int) = this.getOrDefault(key, 0L)