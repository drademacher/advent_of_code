package year2021

import readLines

private var counter = 0

fun main() {
    val input = parseInput(readLines("2021", "day12"))
    val testInput = parseInput(readLines("2021", "day12_test"))

    check(part1(testInput) == 226)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 3509)
    println("Part 2:" + part2(input))
}

private fun parseInput(lines: List<String>): Day12Input {
    val filter =
        lines
            .map { line -> line.split("-") }
            .filter { it.size == 2 }
    return filter
        .fold(hashMapOf()) { acc, edge ->
            acc.putIfAbsent(edge[0], mutableListOf())
            acc[edge[0]]!!.add(edge[1])
            acc.putIfAbsent(edge[1], mutableListOf())
            acc[edge[1]]!!.add(edge[0])
            acc
        }
}

private fun part1(input: Day12Input): Int {
    counter = 0
    dfs(input, "start", mutableListOf("start"), false)
    return counter
}

private fun part2(input: Day12Input): Int {
    counter = 0
    dfs(input, "start", mutableListOf("start"), true)
    return counter
}

private fun dfs(
    input: Day12Input,
    current: String,
    smallCavesVisited: MutableList<String>,
    canVisitSmallCaveTwice: Boolean,
) {
    for (cave in input[current]!!) {
        var canVisitSmallCaveTwice = canVisitSmallCaveTwice
        val isSmallCave = cave[0].isLowerCase()

        if (isSmallCave && smallCavesVisited.contains(cave)) {
            if (canVisitSmallCaveTwice && cave != "start") {
                canVisitSmallCaveTwice = false
            } else {
                continue
            }
        }

        if (cave == "end") {
            counter += 1
        } else {
            smallCavesVisited.add(cave)
            dfs(input, cave, smallCavesVisited, canVisitSmallCaveTwice)
            smallCavesVisited.remove(cave)
        }
    }
}

typealias Day12Input = HashMap<String, MutableList<String>>
