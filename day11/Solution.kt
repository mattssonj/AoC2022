package day11

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    val monkeys = lines.windowed(6, 7).map { MonkeyBuilder.build(it, 3) }

    (1..20).forEach { _ ->
        monkeys.forEach { it.inspectItems(monkeys) }
    }

    val (a, b) = monkeys.map { it.inspections }.sortedDescending().take(2)
    return a * b
}

fun solution2(lines: List<String>): Int {
    return 2
}

object MonkeyBuilder {
    fun build(monkeyInput: List<String>, monkeyBusiness: Int): Monkey {
        val items = parseItems(monkeyInput[1])
        val inspectionFunction = parseInspectionFunction(monkeyInput[2])
        val throwToFunction = parseThrowToFunction(monkeyInput.takeLast(3))

        return Monkey(ArrayDeque(items), 0, throwToFunction, inspectionFunction, monkeyBusiness)
    }

    private fun parseItems(itemString: String): List<Int> {
        return itemString.split(": ")[1].split(",").map { it.trim().toInt() }
    }

    private fun parseInspectionFunction(inspectionString: String): (Int) -> (Int) {
        val parsed = inspectionString.split("= ")[1].trim().split(" ")
        return { input ->
            val first = if (parsed[0] == "old") input else parsed[0].toInt()
            val second = if (parsed[2] == "old") input else parsed[2].toInt()

            val function =
                if (parsed[1] == "*") { a: Int, b: Int -> a * b } else { a: Int, b: Int -> a + b }
            function.invoke(first, second)
        }
    }

    private fun parseThrowToFunction(throwToString: List<String>): (Int) -> (Int) {
        val divideNumber = throwToString.first().trim().split(" ").last().toInt()
        val throwIfTrue = throwToString[1].trim().split(" ")[5].toInt()
        val throwIfFalse = throwToString[2].trim().split(" ")[5].toInt()

        return { input ->
            if (input.mod(divideNumber) == 0) throwIfTrue else throwIfFalse
        }
    }
}

data class Monkey(
    val items: ArrayDeque<Int> = ArrayDeque(),
    var inspections: Int,
    private val throwItemTo: (Int) -> (Int),
    private val evaluateItem: (Int) -> (Int),
    private val monkeyBusiness: Int = 1
) {

    fun inspectItems(possibleReceivers: List<Monkey>) {
        while (items.isNotEmpty()) {
            inspections += 1
            val worriedLevel = inspect(items.removeFirst())
            throwAway(possibleReceivers, worriedLevel)
        }
    }

    private fun inspect(item: Int): Int {
        return evaluateItem.invoke(item) / monkeyBusiness
    }

    private fun throwAway(possibleReceivers: List<Monkey>, item: Int) {
        val throwTo = throwItemTo.invoke(item)
        possibleReceivers[throwTo].receiveItem(item)
    }

    private fun receiveItem(item: Int) {
        items.addLast(item)
    }

}

fun readInput(path: String): List<String> = File(path).readLines()
