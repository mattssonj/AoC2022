package day7

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Long {
    val dirs = buildDirs(lines)
    calc(dirs, "//")
    return dirSizes
}

fun solution2(lines: List<String>): Long {
    val dirs = buildDirs(lines)

    val totalSize = calc(dirs, "//")
    val filesystemSize = 70_000_000

    val sizeLeft = filesystemSize - totalSize
    return getDirsThatCouldBeDeleted(dirs, sizeLeft, "//").minOf { it }
}

fun getDirsThatCouldBeDeleted(dirs: Map<String, String>, sizeLeft: Long, currentDir: String): List<Long> {
    val files = dirs[currentDir]?.split(",") ?: return emptyList()

    val nestedDirs = files.filter { it.startsWith("dir ") }.flatMap { getDirsThatCouldBeDeleted(dirs, sizeLeft, "$currentDir/${it.drop(4)}") }

    val thisSize = calc(dirs, currentDir)

    val afterDeleted = sizeLeft + thisSize
    return if (afterDeleted >= 30_000_000) nestedDirs + thisSize
    else emptyList()
}

private fun buildDirs(lines: List<String>): Map<String, String> {
    var currentDir = ""
    val dirs = mutableMapOf<String, String>()
    val deque = ArrayDeque(lines)
    while (deque.isNotEmpty()) {
        val (command, output) = parse(deque)
        when (command) {
            Command.CD -> currentDir = if (output == "..") currentDir.dropLastWhile { it != '/' }
                .dropLast(1) else "$currentDir/$output"

            Command.LS -> dirs[currentDir] = output
        }
    }
    return dirs
}

private fun parse(queue: ArrayDeque<String>): Pair<Command, String> {
    val commandString = queue.removeFirst()
    val command = commandString.toCommand()

    if (command == Command.LS) {
        val files = mutableListOf<String>()
        var next = queue.firstOrNull()
        while (next != null && !next.startsWith("$")) {
            files.add(queue.removeFirst())
            next = queue.firstOrNull()
        }
        return Pair(Command.LS, files.joinToString(","))
    }
    return Pair(Command.CD, commandString.replace("$ cd ", ""))
}

private fun String.toCommand(): Command {
    return when (this.take(4)) {
        "$ ls" -> Command.LS
        "$ cd" -> Command.CD
        else -> throw IllegalArgumentException("$this is not a command")
    }
}

enum class Command {
    LS,
    CD
}

// Used for part 1
var dirSizes = 0L

private fun calc(map: Map<String, String>, currentDir: String): Long {
    val files = map[currentDir]?.split(",") ?: return 0

    val nestedDirSizes = files.filter { it.startsWith("dir ") }
        .map { calc(map, "$currentDir/${it.drop(4)}") }
    dirSizes += nestedDirSizes.filter { it <= 100_000 }.sum()

    val filesInThisDirSize = files.filter { !it.startsWith("dir ") }.sumOf { it.split(" ").first().toLong() }

    return nestedDirSizes.sum() + filesInThisDirSize
}

fun readInput(path: String): List<String> = File(path).readLines()
