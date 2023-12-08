package year2023

import readInput

fun main() {
    val input = readInput("2023", "day02")
    val testInput = readInput("2023", "day02_test")

    check(part1(testInput) == 8)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 2286)
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): List<Game> {
    fun parseCubeCount(string: String): CubeCount {
        val rawCubePairs = string.split(", ").map { rawCube -> rawCube.split(" ").let { Pair(it[0], it[1]) } }

        return CubeCount(
            red = rawCubePairs.find { it.second == "red" }?.first?.toInt() ?: 0,
            blue = rawCubePairs.find { it.second == "blue" }?.first?.toInt() ?: 0,
            green = rawCubePairs.find { it.second == "green" }?.first?.toInt() ?: 0,
        )
    }

    fun parseGame(string: String): Game {
        val number = string.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt()

        val rawCubeCounts = string.dropWhile { it != ':' }.drop(2).split("; ")
        val cubeCounts = rawCubeCounts.map { parseCubeCount(it) }

        return Game(number, cubeCounts)
    }

    return input.map { parseGame(it) }
}

private fun part1(input: List<String>): Int {
    val input = parseInput(input)

    return input
        .filter { game -> game.cubeCounts.all { cubeCount -> cubeCount.red <= 12 && cubeCount.green <= 13 && cubeCount.blue <= 14 } }
        .sumOf { it.index }
}

private fun part2(input: List<String>): Int {
    val input = parseInput(input)

    return input
        .map { game -> game.cubeCountMaximums() }
        .sumOf { maximums -> maximums.red * maximums.blue * maximums.green }
}

data class Game(val index: Int, val cubeCounts: List<CubeCount>) {
    fun cubeCountMaximums(): CubeCount {
        return CubeCount(
            cubeCounts.maxBy { it.red }.red,
            cubeCounts.maxBy { it.blue }.blue,
            cubeCounts.maxBy { it.green }.green,
        )
    }
}

data class CubeCount(val red: Int, val blue: Int, val green: Int)
