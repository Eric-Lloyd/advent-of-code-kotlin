package day6

import java.io.File

fun main() {
    val initial = File("src", "day6/Day6.txt").readLines()[0].split(",").map { it.toInt() }
    val count1 = Fish(initial).countAfter(80)
    val count2 = Fish(initial).countAfter(256)
    println(count1)
    println(count2)
}


class Fish(initial: List<Int>) {
    private var fishMap: Map<Int, Long> = initial.groupingBy { it }.eachCount() as Map<Int, Long>

    fun countAfter(days: Int): Long {
        repeat(days) { update() }
        return fishMap.values.sum()
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

private fun Map<Int, Long>.value(key: Int) = getOrDefault(key, 0L)