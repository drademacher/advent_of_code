package year2021

import readFile

fun main() {
    val input = parseInput(readFile("2021", "day6"))
    val testInput = parseInput(readFile("2021", "day6_test"))

    check(part1(testInput) == 5934L)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 26984457539L)
    println("Part 2:" + part2(input))
}

private fun parseInput(string: String): List<Int> {
    return string.takeWhile { it != '\n' }.split(",").map { it.toInt() }
}

private fun part1(input: List<Int>): Long {
    return lanternfish(input, 80)
}

private fun part2(input: List<Int>): Long {
    return lanternfish(input, 256)
}

private fun lanternfish(
    inputs: List<Int>,
    days: Int,
): Long {
    var current = Array(9) { 0L }
    var next: Array<Long>

    for (input in inputs) {
        current[input] += 1L
    }

    for (i in 1..days) {
        next = Array(9) { if (it < 8) current[it + 1] else 0 }
        next[8] += current[0]
        next[6] += current[0]
        current = next
    }

    return current.sum()
}
