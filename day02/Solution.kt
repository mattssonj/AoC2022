package day2

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    return lines.sumOf {
        val (op, me) = it.split(" ")
        val myHand = Hand.from(me)
        val opHand = Hand.from(op)
        myHand.play(opHand)
    }
}

fun solution2(lines: List<String>): Int {
    return lines.sumOf {
        val (op, outcome) = it.split(" ")
        val opHand = Hand.from(op)
        val handINeedForOutcome = when (outcome) {
            LOSE -> opHand.getHandThatWouldLose()
            WIN -> opHand.getHandThatWouldWin()
            else -> opHand
        }
        handINeedForOutcome.play(opHand)
    }
}

private const val LOSE = "X"
private const val WIN = "Z"

private enum class Hand(
    private val signs: List<String> = emptyList(),
    val point: Int,
    private val losesTo: Int
) {
    ROCK(listOf("A", "X"), 1, 1),
    PAPER(listOf("B", "Y"), 2, 2),
    SCISSOR(listOf("C", "Z"), 3, 0);

    companion object {
        fun from(s: String): Hand = values().first { it.signs.contains(s) }
    }

    fun play(hand: Hand): Int {
        if (this == hand) return 3 + point
        val iLoseTo = values()[losesTo]
        if (iLoseTo == hand) return point
        return 6 + point
    }

    fun getHandThatWouldWin() = values()[losesTo]
    fun getHandThatWouldLose() = values().first { it != getHandThatWouldWin() && it != this }
}

fun readInput(path: String): List<String> = File(path).readLines()
