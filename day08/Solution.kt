package day8

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    val result = isVisible(lines).filter { it }.size
    val edges = lines.size * 2 + lines.first().length * 2 - 4
    return result + edges
}

fun isVisible(map: List<String>): List<Boolean> {
    val horizontalRange = (1 until map.first().length - 1)
    val verticalRange = (1 until map.size - 1)

    val result = verticalRange.flatMap { vertical ->
        horizontalRange.map { horizontal ->
            val currentTree = Pair(vertical, horizontal)
            val isVisibleLeft = isVisibleInHorizontalDirection(map, (0 until horizontal), currentTree)
            val isVisibleRight = isVisibleInHorizontalDirection(map, (map.size - 1 downTo horizontal + 1), currentTree)
            val isVisibleUp = isVisibleInVerticalDirection(map, (0 until vertical), currentTree)
            val isVisibleDown =
                isVisibleInVerticalDirection(map, (map.first().length - 1 downTo vertical + 1), currentTree)

            val isVisible = isVisibleRight or isVisibleLeft or isVisibleUp or isVisibleDown
            val currentSize = map[vertical][horizontal].toInteger()
            println("currentTree = ${currentTree} with size = $currentSize " + if (isVisible) "is visible" else "is not visible")
            println("DEBUG: left = $isVisibleLeft, right = $isVisibleRight, up = $isVisibleUp, down = $isVisibleDown")
            isVisible
        }
    }
    return result
}

private fun Char.toInteger() = this.toString().toInt()

fun isVisibleInVerticalDirection(map: List<String>, range: IntProgression, tree: Pair<Int, Int>): Boolean {
    val (vertical, horizontal) = tree
    val currentTreeSize = map[vertical][horizontal].toInteger()
    val anyBigger = range.firstOrNull {
        map[it][horizontal].toInteger() >= currentTreeSize
    }
    return anyBigger == null
}

fun isVisibleInHorizontalDirection(map: List<String>, range: IntProgression, tree: Pair<Int, Int>): Boolean {
    val (vertical, horizontal) = tree
    val currentTreeSize = map[vertical][horizontal].toInteger()
    val anyBigger = range.firstOrNull {
        map[vertical][it].toInteger() >= currentTreeSize
    }
    return anyBigger == null
}


fun solution2(lines: List<String>): Long {
    return 2
}

fun readInput(path: String): List<String> = File(path).readLines()
