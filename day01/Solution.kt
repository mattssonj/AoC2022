import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    var highest = 0
    var current = 0
    lines.forEach {
        if (it.isBlank()) {
            if (highest < current) highest = current
            current = 0
        } else current += it.toInt()
    }
    return highest
}

fun solution2(lines: List<String>): Int {
    val calories = mutableListOf<Int>()
    var current = 0
    lines.forEachIndexed { index, s ->
        if (s.isBlank()) {
            calories.add(current)
            current = 0
        } else if (index + 1 == lines.size) {
            current += s.toInt()
            calories.add(current)
        } else current += s.toInt()
    }
    return calories.sortedDescending().take(3).sum()
}

fun readInput(path: String): List<String> = File(path).readLines()
