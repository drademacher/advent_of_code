package year2021

import readLines

fun main() {
    val input = readLines("2021", "day1").map { it.toInt() }
    val testInput = readLines("2021", "day1_test").map { it.toInt() }

    check(part1(testInput) == 7)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 5)
    println("Part 2:" + part2(input))
}

private fun part1(input: List<Int>): Int {
    return (0..input.size - 2)
        .filter { input[it + 1] > input[it] }
        .size
}

private fun part2(input: List<Int>): Int {
    val measurements = (0..input.size - 3)
        .map { input[it] + input[it + 1] + input[it + 2] }

    return part1(measurements)
}
