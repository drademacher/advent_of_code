package year2023

import powerOfTwo
import readLines

fun main() {
    val input = parseInput(readLines("2023", "day4"))
    val testInput = parseInput(readLines("2023", "day4_test"))

    check(part1(testInput) == 13)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 30)
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): List<Card> {
    return input
        .map { it.split(Regex("[:|]")) }
        .map {
            Card(
                it[1].split(" ").filter { it != "" }.map(String::toInt),
                it[2].split(" ").filter { it != "" }.map(String::toInt),
            )
        }
}

private fun part1(input: List<Card>): Int {
    return input
        .map { card -> card.numbers.filter { number -> card.winningNumbers.contains(number) }.size }
        .filter { it >= 1 }
        .sumOf { powerOfTwo(it - 1) }
}

private fun part2(input: List<Card>): Int {
    val instances = input.indices.map { Pair(it, 1) }.toMap().toMutableMap()
    for (i in input.indices) {
        val card = input[i]
        val count = instances[i]!!
        val numberOfWonCards = card.numbers.filter { number -> card.winningNumbers.contains(number) }.size

        for (j in (1..numberOfWonCards)) {
            val k = i + j
            val oldValue = instances.getOrDefault(k, 1)
            instances[k] = oldValue + count
        }
    }

    return instances.values.sum()
}

private data class Card(val winningNumbers: List<Int>, val numbers: List<Int>)
