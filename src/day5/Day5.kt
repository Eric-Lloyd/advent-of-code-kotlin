package day5

import readInput
import kotlin.math.abs

fun main() {
    val lines = readInput("day5/Day5")
        .map { lineFrom(it) }
    val count1 = count(lines) { line -> line.isVertical || line.isHorizontal }
    val count2 = count(lines) { line -> line.isVertical || line.isHorizontal || line.isDiagonal }
    println(count1)
    println(count2)
}

fun count(lines: List<Line>, filter: (Line) -> Boolean): Int {
    return overlappingPoints(lines.filter { filter(it) }, 2)
}

fun overlappingPoints(lines: List<Line>, threshold: Int): Int {
    val counts = mutableMapOf<Point, Int>()
    for (line in lines) {
        for (point in line.points()) {
            counts[point] = (counts[point] ?: 0) + 1
        }
    }
    return counts.count { (_, count) -> count >= threshold }
}

data class Point(val x: Int, val y: Int)

data class Line(val from: Point, val to: Point) {
    val isHorizontal: Boolean = from.y == to.y
    val isVertical: Boolean = from.x == to.x
    val isDiagonal: Boolean = abs(from.x - to.x) == abs(from.y - to.y)

    /*
     * returns all the points along the line.
     */
    fun points(): List<Point> {
        val points = mutableListOf<Point>()
        when {
            isHorizontal -> {
                val min = minOf(from.x, to.x)
                val max = maxOf(from.x, to.x)
                for (i in min until max + 1) {
                    points.add(Point(i, from.y))
                }
                return points
            }
            isVertical -> {
                val min = minOf(from.y, to.y)
                val max = maxOf(from.y, to.y)
                for (i in min until max + 1) {
                    points.add(Point(from.x, i))
                }
                return points
            }
            isDiagonal -> {
                points.add(from)
                val xDirection = direction(from.x, to.x) // whether to increment or decrement x when traversing the line
                val yDirection = direction(from.y, to.y) // whether to increment or decrement y when traversing the line
                var currentX = xDirection(from.x)
                var currentY = yDirection(from.y)
                while (currentX != to.x) {
                    points.add(Point(currentX, currentY))
                    currentX = xDirection(currentX)
                    currentY = yDirection(currentY)
                }
                points.add(to)
                return points
            }
            else -> {
                throw NotImplementedError("We only support horizontal, vertical or diagonal lines.")
            }
        }
    }
}

fun direction(a1: Int, a2: Int): (Int) -> Int {
    if (a1 > a2) return { a -> a - 1 } else return { a -> a + 1 }
}

fun lineFrom(line: String) =
    line.split(" -> ")
        .map { subLine ->
            subLine.split(",")
                .map { it.toInt() }
                .let { (x, y) -> Point(x, y) }
        }
        .let { (from, to) -> Line(from, to) }