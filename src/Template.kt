package year2021

import readLines

fun main() {
    val input = readLines("2021", "day")
    val testInput = readLines("2021", "day_test")

    check(part1(testInput) == 0)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 0)
    println("Part 2:" + part2(input))
}

private fun part1(input: List<String>): Int {
    return 0
}

private fun part2(input: List<String>): Int {
    return 0
}
