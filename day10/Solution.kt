package day10

import java.io.File

fun main() {
    when (System.getenv("part")) {
        "part2" -> println(solution2(readInput("input.txt")))
        else -> println(solution1(readInput("input.txt")))
    }
}

fun solution1(lines: List<String>): Int {
    val cpu = CPU()
    lines.forEach { cpu.process(it) }
    return cpu.signalStrengths
}

fun solution2(lines: List<String>): String {
    val cpu = CPU()
    lines.forEach { cpu.process(it) }
    cpu.crt.print()
    return "rzhfgjcb"
}

class CPU(
    private var registerX: Int = 1,
    private var cycles: Int = 0,
    var signalStrengths: Int = 0,
    val crt: CRT = CRT()
) {

    fun process(operation: String) {
        when (operation.take(4)) {
            "noop" -> clock()
            "addx" -> add(operation.drop(5).toInt())
        }
    }

    private fun clock() {
        this.cycles += 1
        crt.render(registerX)
        if ((cycles - 20) % 40 == 0) {
            signalStrengths += cycles * registerX
        }
    }

    private fun add(amount: Int) {
        clock()
        clock()
        registerX += amount
    }
}

class CRT {

    private val screen: List<CharArray>
    private var renderPosition = 0

    init {
        val emptyScreen =
            (1..6).map { (0..40).map { "" }.joinToString(separator = " ").toCharArray() }
        this.screen = emptyScreen
    }

    fun render(spritePos: Int) {
        val sprite = (spritePos - 1..spritePos + 1)

        val currentRowPosition = renderPosition % 40
        if (currentRowPosition in sprite) drawPixel()
        renderPosition += 1
    }

    private fun drawPixel() {
        val row = renderPosition / 40
        val position = renderPosition % 40
        screen[row][position] = '#'
    }

    fun print() {
        screen.forEach { println(it.joinToString(separator = "")) }
    }
}

fun readInput(path: String): List<String> = File(path).readLines()
