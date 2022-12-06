package day5

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): String {
    return solve(craneModel = "9000", lines)
}

fun solution2(lines: List<String>): String {
    return solve(craneModel = "9001", lines)
}

fun solve(craneModel: String, lines: List<String>): String {
    val cargoInput = lines.takeWhile { it.isNotBlank() }
        .reversed()
        .map { it.windowed(3, 4).map { it.trim() } }


    val craneOperation = lines.drop(cargoInput.size + 1)

    val cargo = Cargo.create(cargoInput)
    craneOperation.forEach { Crane.move(cargo, it, craneModel) }

    return cargo.stacks.joinToString(separator = "") { it.getTop() }
        .replace("[", "")
        .replace("]", "")
        .replace(",", "")
}

object Crane {
    fun move(cargo: Cargo, operation: String, model: String = "9000") {
        val split = operation.split(" ")
        val boxesToMove = split[1].toInt()
        val moveFrom = split[3].toInt() - 1
        val moveTo = split[5].toInt() - 1

        when (model) {
            "9000" -> cargo.move(boxesToMove, moveFrom, moveTo)
            "9001" -> cargo.moveMany(boxesToMove, moveFrom, moveTo)
            else -> throw IllegalArgumentException("Can only operate as model 9000 or 9001")
        }

    }
}

data class Cargo(val stacks: List<Stack>) {
    companion object {
        fun create(input: List<List<String>>): Cargo {
            val stacks = input.first().map { Stack.create(it) }
            input.drop(1).forEach {
                it.forEachIndexed { index, crate ->
                    if (crate.isNotBlank()) stacks[index].addCrate(crate)
                }
            }
            return Cargo(stacks)
        }
    }

    fun move(numberOfBoxes: Int, fromStack: Int, toStack: Int) {
        val crates = stacks[fromStack].take(numberOfBoxes)
        stacks[toStack].place(crates)
    }

    fun moveMany(numberOfBoxes: Int, fromStack: Int, toStack: Int) {
        val crates = stacks[fromStack].takeMany(numberOfBoxes)
        stacks[toStack].place(crates)
    }
}

data class Stack(val queue: ArrayDeque<String>) {
    companion object {
        fun create(rowNumber: String): Stack {
            val queue = ArrayDeque<String>()
            queue.addLast(rowNumber)
            return Stack(queue)
        }
    }

    fun addCrate(crate: String) {
        queue.addFirst(crate)
    }

    fun take(crates: Int): List<String> {
        return (1..crates).map { queue.removeFirst() }
    }

    fun takeMany(crates: Int): List<String> {
        return take(crates).reversed()
    }

    fun place(crates: List<String>) {
        crates.forEach { queue.addFirst(it) }
    }

    fun getTop(): String {
        return queue.first()
    }
}

fun readInput(path: String): List<String> = File(path).readLines()
