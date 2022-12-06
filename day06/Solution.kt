package day6

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    return solve(lines, 4)
}

fun solution2(lines: List<String>): Int {
    return solve(lines, 14)
}

fun solve(lines: List<String>, numberOfDistinct: Int): Int {
    return lines.first()
        .windowed(numberOfDistinct, 1)
        .indexOfFirst { it.toSet().size == it.length }
        .let { it + numberOfDistinct }
}

fun readInput(path: String): List<String> = File(path).readLines()
