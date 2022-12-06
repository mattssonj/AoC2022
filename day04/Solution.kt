package day4

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    return lines.map { it.split(",") }
        .map { assignedToSameSection(it, Boolean::and) }
        .map { if (it) 1 else 0 }
        .sum()
}

private fun assignedToSameSection(
    assignments: List<String>,
    op: (Boolean, Boolean) -> Boolean
): Boolean {
    val (first, second) = assignments.map { it.split("-") }
    return first.containsAssignment(second, op) || second.containsAssignment(first, op)
}

private fun List<String>.containsAssignment(
    list: List<String>,
    op: (Boolean, Boolean) -> Boolean
): Boolean {
    val (f1, f2) = this.map { it.toInt() }
    val (s1, s2) = list.map { it.toInt() }

    val thisAssignment = f1..f2
    return op.invoke(s1 in thisAssignment, s2 in thisAssignment)
}

fun solution2(lines: List<String>): Int {
    return lines.map { it.split(",") }
        .map { assignedToSameSection(it, Boolean::or) }
        .map { if (it) 1 else 0 }
        .sum()
}

fun readInput(path: String): List<String> = File(path).readLines()
