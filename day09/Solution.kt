package day9

import java.io.File
import kotlin.math.abs

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    val head = Coordinate(0,0)
    val tail = Coordinate(0,0)
    lines.forEach {
        val (direction, steps) = it.split(" ")
        (1..steps.toInt()).forEach { _ ->
            head.move(direction)
            if (!tail.isTouching(head)) {
                tail.moveCloser(head)
            }
        }
    }

    return tail.history.size
}

fun solution2(lines: List<String>): Int {
    val knots = (1..10).map { Coordinate(0,0) }

    lines.forEach {
        val (direction, steps) = it.split(" ")
        (1..steps.toInt()).forEach {
            knots.first().move(direction)
            knots.windowed(2).forEach { (head, tail) ->
                if (!tail.isTouching(head)) {
                    tail.moveCloser(head)
                }
            }
        }
    }

    return knots.last().history.size
}

data class Coordinate(var x: Int, var y: Int, val history: MutableSet<Pair<Int, Int>> = mutableSetOf()) {

    init {
        storeCurrentPosition()
    }

    private fun storeCurrentPosition() {
        val currentPos = Pair(x, y)
        history.add(currentPos)
    }

    fun isTouching(other: Coordinate) = abs(this.x - other.x) <= 1 && abs(this.y - other.y) <= 1
    fun move(direction: String) {
        when (direction) {
            "U" -> this.y += 1
            "D" -> this.y -= 1
            "R" -> this.x += 1
            "L" -> this.x -= 1
            else -> throw IllegalArgumentException("Unable to move with direction = $direction")
        }
        storeCurrentPosition()
    }

    fun moveCloser(other: Coordinate) {
        val xDiff = other.x - this.x
        val stepX = if (xDiff == 0) 0 else if (xDiff > 0) 1 else -1
        this.x += stepX

        val yDiff = other.y - this.y
        val stepY = if (yDiff == 0) 0 else if (yDiff > 0) 1 else -1
        this.y += stepY

        storeCurrentPosition()
    }
}

fun readInput(path: String): List<String> = File(path).readLines()
