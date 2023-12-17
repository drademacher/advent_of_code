package year2023

import Grid
import readLines

fun main() {
    val input = parseInput(readLines("2023", "day14"))
    val testInput = parseInput(readLines("2023", "day14_test"))

    val testResultPart1 = part1(testInput)
    check(testResultPart1 == 136) { "Incorrect result for part 1: $testResultPart1" }
    println("Part 1:" + part1(input))

    val testResultPart2 = part2(testInput)
    check(testResultPart2 == 0) { "Incorrect result for part 2: $testResultPart2" }
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): Grid<Char> {
    return Grid(input.map { it.toCharArray().toList() })
}

private fun part1(grid: Grid<Char>): Int {
    var result = 0

    for (col in (0..<grid.cols).map { grid.nthCol(it) }) {
        var countRoundedRocks = 0
        var height = grid.rows

        for ((index, obj) in col.toCharArray().withIndex()) {
            when (obj) {
                'O' -> {
                    countRoundedRocks++
                }

                '#' -> {
                    result += (height - countRoundedRocks + 1..height).sum()

                    height = grid.rows - index - 1
                    countRoundedRocks = 0
                }

                else -> {}
            }
        }

        result += (height - countRoundedRocks + 1..height).sum()
    }

    return result
}

private fun part2(grid: Grid<Char>): Int {
    return 0
}
