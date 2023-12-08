package year2021

import readLines

fun main() {
    val input = parseInput(readLines("2021", "day14").filter { it != "" })
    val testInput = parseInput(readLines("2021", "day14_test").filter { it != "" })

    check(part1(testInput) == 1588L)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 2188189693529L)
    println("Part 2:" + part2(input))
}

private fun parseInput(lines: List<String>): Day14Input {
    val template =
        (0..lines.first().length - 2)
            .map { "${lines.first()[it]}${lines.first()[it + 1]}" }
            .groupBy { it }
            .map { Pair(it.value.first(), it.value.size.toLong()) }
            .toMap()
    val rules =
        lines
            .drop(1)
            .map { it.split(" -> ") }
            .associate { Pair(it[0], it[1]) }

    return Day14Input(template, rules)
}

private fun part1(input: Day14Input): Long {
    return solveExtendedPolymerization(input, 10)
}

private fun part2(input: Day14Input): Long {
    return solveExtendedPolymerization(input, 40)
}

private fun solveExtendedPolymerization(
    input: Day14Input,
    steps: Int,
): Long {
    var current = input.template.toMutableMap()

    repeat(steps) {
        val next = mutableMapOf<String, Long>()
        for ((key, value) in current) {
            if (input.rules[key] != null) {
                val first = "${key[0]}${input.rules[key]}"
                val second = "${input.rules[key]}${key[1]}"
                next[first] = next.getOrDefault(first, 0) + value
                next[second] = next.getOrDefault(second, 0) + value
            } else {
                next[key] = next.getOrDefault(key, 0) + value
            }
        }
        current = next
    }

    val occurrences =
        current
            .flatMap { (key, value) -> listOf(Pair(key[0], value), Pair(key[1], value)) }
            .groupBy { it.first }
            .map { (it.value.sumOf { it.second } + 1) / 2 }
    return occurrences.maxOf { it } - occurrences.minOf { it }
}

data class Day14Input(val template: Map<String, Long>, val rules: Map<String, String>)
