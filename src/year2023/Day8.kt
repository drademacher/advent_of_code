package year2023

import readLines

fun main() {
    val input = parseInput(readLines("2023", "day8"))
    val testInputPart1 = parseInput(readLines("2023", "day8_test1"))
    val testInputPart2 = parseInput(readLines("2023", "day8_test2"))

    check(part1(testInputPart1) == 2)
    println("Part 1:" + part1(input))

    check(part2(testInputPart2) == 6L)
    println("Part 2:" + part2(input))
}

private fun parseInput(lines: List<String>): WalkingInstructionsInput {
    val cleanedMainLines = lines.drop(2).map { line -> line.filter { it.isLetterOrDigit() || it == ',' || it == '=' } }
    val map = mutableMapOf<String, Pair<String, String>>()
    for (line in cleanedMainLines) {
        val splittedLine = line.split(Regex("[=,]"), 0)
        check(splittedLine.size == 3)
        map[splittedLine[0]] = Pair(splittedLine[1], splittedLine[2])
    }

    return WalkingInstructionsInput(
        lines[0],
        map,
    )
}

private fun part1(input: WalkingInstructionsInput): Int {
    return input.findExit("AAA", { it == "ZZZ" })
}

private fun part2(input: WalkingInstructionsInput): Long {
    val startingNodes = input.simpleGraph.keys.filter { it.endsWith("A") }

    // inputs seems to be tailored to have constant loops
    val loopDurationDivisors =
        startingNodes
            .asSequence()
            .map { startingNode -> input.findExit(startingNode, { it.endsWith("Z") }) }
            .map { loopLength -> getAllDivisors(loopLength) }

    // does fail on corner cases
    val commonMergedDivisors =
        loopDurationDivisors
            .flatten()
            .toSet()

    return commonMergedDivisors.map { it.toLong() }.reduce { acc, current -> acc * current }
}

private fun getAllDivisors(int: Int): MutableList<Int> {
    val result = mutableListOf<Int>()
    var remaining = int

    for (divisor in (2..remaining)) {
        while (remaining % divisor == 0) {
            result.add(divisor)
            remaining /= divisor
        }
    }

    return result
}

private data class WalkingInstructionsInput(val instructions: String, val simpleGraph: Map<String, Pair<String, String>>) {
    fun findExit(
        start: String,
        isGoalReached: (String) -> Boolean,
    ): Int {
        var current = start
        var steps = 0

        do {
            val instruction = instructions[steps % instructions.length]
            val node = simpleGraph[current]!!
            current = if (instruction == 'R') node.second else node.first

            steps += 1
        } while (!isGoalReached(current))

        return steps
    }
}
