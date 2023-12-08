package year2021

import readLines
import kotlin.math.abs

fun main() {
    val input = readLines("2021", "day7").map { it.toInt() }
    val testInput = readLines("2021", "day7_test").map { it.toInt() }

    check(part1(testInput) == 37)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 168)
    println("Part 2:" + part2(input))
}

private fun part1(input: List<Int>): Int {
    return (input.min()..input.max())
        .map { goal -> Pair(goal, input.sumOf { abs(it - goal) }) }
        .minByOrNull { it.second }!!
        .second
}

private fun part2(input: List<Int>): Int {
    return (input.min()..input.max())
        .map { goal -> Pair(goal, input.sumOf { gauss(abs(it - goal)) }) }
        .minByOrNull { it.second }!!
        .second
}

private fun gauss(n: Int): Int = n * (n + 1) / 2
