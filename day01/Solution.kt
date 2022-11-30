import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("day05/input.txt")))
        else -> println(solution1(readInput("day05/input.txt")))
    }
}

fun solution1(points: Points): Int {
 return 1
}

fun solution2(points: Points): Int {
    return 2
}

fun readInput(path: String): Points = File(path).readLines()
    .map { it.split(" -> ").flatMap { it.split(",").chunked(2).map { Pair(it[0].toInt(), it[1].toInt()) } } }
    .map { Pair(it[0], it[1]) }

typealias Points = List<Pair<Point, Point>>
typealias Point = Pair<Int, Int>
