package day8

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    val result = calc(lines).filter { (seen, _) -> seen }.size
    val edges = lines.size * 2 + lines.first().length * 2 - 4
    return result + edges
}

fun solution2(lines: List<String>): Int {
    return calc(lines).maxOf { (_, numberOfTrees) -> numberOfTrees }
}

fun calc(map: List<String>): List<Pair<Boolean, Int>> {
    val horizontalRange = (1 until map.first().length - 1)
    val verticalRange = (1 until map.size - 1)

    val result = verticalRange.flatMap { vertical ->
        horizontalRange.map { horizontal ->
            val currentTree = Pair(vertical, horizontal)
            val isVisibleLeft = isVisibleInHorizontalDirection(map, (horizontal - 1 downTo 0), currentTree)
            val isVisibleRight = isVisibleInHorizontalDirection(map, (horizontal + 1 until map.first().length), currentTree)
            val isVisibleUp = isVisibleInVerticalDirection(map, (vertical - 1 downTo 0), currentTree)
            val isVisibleDown = isVisibleInVerticalDirection(map, (vertical + 1 until map.size), currentTree)

            val isVisible = isVisibleRight.first or isVisibleLeft.first or isVisibleUp.first or isVisibleDown.first
            val treesSeen = isVisibleRight.second * isVisibleLeft.second * isVisibleUp.second * isVisibleDown.second
            Pair(isVisible, treesSeen)
        }
    }
    return result
}

private fun Char.toInteger() = this.toString().toInt()

fun isVisibleInVerticalDirection(map: List<String>, range: IntProgression, tree: Pair<Int, Int>): Pair<Boolean, Int> {
    val (vertical, horizontal) = tree
    val currentTreeSize = map[vertical][horizontal].toInteger()
    val anyBigger = range.map {
        if (map[it][horizontal].toInteger() >= currentTreeSize) null else 1
    }

    val containsNull = anyBigger.contains(null)
    return Pair(!containsNull, anyBigger.takeWhile { it != null }.size + if (containsNull) 1 else 0)
}

fun isVisibleInHorizontalDirection(map: List<String>, range: IntProgression, tree: Pair<Int, Int>): Pair<Boolean, Int> {
    val (vertical, horizontal) = tree
    val currentTreeSize = map[vertical][horizontal].toInteger()

    val anyBigger = range.map {
        if (map[vertical][it].toInteger() >= currentTreeSize) null else 1
    }
    val containsNull = anyBigger.contains(null)
    return Pair(!containsNull, anyBigger.takeWhile { it != null }.size + if (containsNull) 1 else 0)
}

fun readInput(path: String): List<String> = File(path).readLines()
