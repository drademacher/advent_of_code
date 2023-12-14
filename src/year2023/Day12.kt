package year2023

import readLines

fun main() {
    val input = readLines("2023", "day12")
    val testInput = readLines("2023", "day12_test")

    check(part1(testInput) == 21L)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 525152L)
    println("Part 2:" + part2(input))
}

private fun part1(input: List<String>): Long {
    val arrangements =
        input.map { row ->
            memory = HashMap()

            val visualMap = row.takeWhile { !it.isWhitespace() }
            val groups = row.dropWhile { !it.isDigit() }.split(",").map { it.toInt() }

            calculateNumberOfArrangements(visualMap, 0, groups)
        }

    return arrangements.sum()
}

private fun part2(input: List<String>): Long {
    val arrangementsWithUnfoldedInput =
        input.map { row ->
            memory = HashMap()

            val visualMap = row.takeWhile { !it.isWhitespace() }
            val groups = row.dropWhile { !it.isDigit() }.split(",").map { it.toInt() }

            val unfoldedVisualMap = (1..5).joinToString("?") { visualMap }
            val unfoldedGroups = (1..5).flatMap { groups }

            calculateNumberOfArrangements(unfoldedVisualMap, 0, unfoldedGroups)
        }

    return arrangementsWithUnfoldedInput.sum()
}

private var memory = HashMap<Pair<Int, List<Int>>, Long>()

private fun calculateNumberOfArrangements(
    visualMap: String,
    currentIndex: Int,
    currentGroups: List<Int>,
): Long {
    if (currentIndex == visualMap.length && currentGroups.isEmpty()) {
        return 1
    } else if (currentIndex >= visualMap.length) {
        return 0
    }

    if (memory[Pair(currentIndex, currentGroups)] != null) {
        return memory[Pair(currentIndex, currentGroups)]!!
    }

    val result =
        when (visualMap[currentIndex]) {
            '.' -> calculateForCurrentIsDot(visualMap, currentIndex, currentGroups)
            '#' -> calculateForCurrentIsHash(visualMap, currentIndex, currentGroups)
            '?' -> calculateForCurrentIsDot(visualMap, currentIndex, currentGroups) + calculateForCurrentIsHash(visualMap, currentIndex, currentGroups)
            else -> throw IllegalStateException("s[current]=${visualMap[currentIndex]} is illegal")
        }

    memory[Pair(currentIndex, currentGroups)] = result

    return result
}

private fun calculateForCurrentIsDot(
    visualMap: String,
    currentIndex: Int,
    currentGroups: List<Int>,
): Long {
    return calculateNumberOfArrangements(visualMap, currentIndex + 1, currentGroups)
}

private fun calculateForCurrentIsHash(
    visualMap: String,
    currentIndex: Int,
    currentGroups: List<Int>,
): Long {
    if (currentGroups.isEmpty()) {
        return 0
    }

    val firstGroup = currentGroups.first()

    if (currentIndex + firstGroup > visualMap.length) {
        return 0
    }

    val nextGroupIsNotDot = visualMap.drop(currentIndex).take(firstGroup).all { it == '#' || it == '?' }
    val isLastGroup = currentGroups.size == 1
    val nextSymbolIsNotHash = currentIndex + firstGroup == visualMap.length || visualMap[currentIndex + firstGroup] != '#'

    if (nextGroupIsNotDot && (isLastGroup || nextSymbolIsNotHash)) {
        val offset = if (isLastGroup) 0 else 1
        return calculateNumberOfArrangements(visualMap, currentIndex + firstGroup + offset, currentGroups.drop(1))
    }

    return 0
}
