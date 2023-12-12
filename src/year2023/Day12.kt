package year2023

import readLines

fun main() {
    val input = parseInput(readLines("2023", "day12"))
    val testInput = parseInput(readLines("2023", "day12_test"))

    check(part1(testInput) == 21)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 525152)
    println("Part 2:" + part2(input))

}

private fun parseInput(input: List<String>): List<String> {
    return input
}

private fun part1(input: List<String>): Int {
    val arrangements = input.map { row ->
        val springMap = row.takeWhile { !it.isWhitespace() }
        val groups = row.dropWhile { !it.isDigit() }.split(",").map { it.toInt() }

        val placeableHash = groups.sum() - springMap.count { it == '#' }
        val placeableDots = springMap.count { it == '?' } - placeableHash

        recurse(springMap, groups, placeableHash, placeableDots)
    }

    return arrangements.sum()

}

private fun recurse(s: String, groups: List<Int>, placeableHash: Int, placeableDots: Int): Int {
    if (placeableHash == 0 && placeableDots == 0) {
        return if (magic(s, groups)) 1 else 0
    }

    var res = 0
    if (placeableDots > 0) {
        res += recurse(s.replaceFirst('?', '.'), groups, placeableHash, placeableDots - 1)
    }

    if (placeableHash > 0) {
        res += recurse(s.replaceFirst('?', '#'), groups, placeableHash - 1, placeableDots)
    }

    return res
}

private fun magic(s: String, groups: List<Int>): Boolean {
    var mutableString = s.dropWhile { it != '#' }.dropLastWhile { it != '#' } + "."

    for (group in groups) {

        val groupOfHash = mutableString.takeWhile { it == '#' }.length
        val groupOfDotAfterwards = mutableString.dropWhile { it == '#' }.takeWhile { it == '.' }.length

        if (groupOfHash != group || groupOfDotAfterwards == 0) {
            return false
        }

        mutableString = mutableString.drop(groupOfHash + groupOfDotAfterwards)
    }

    return true
}

private fun part2(input: List<String>): Int {
    val newInput = input.map {row ->
        val springMap = row.takeWhile { !it.isWhitespace() }
        val groups = row.dropWhile { !it.isDigit() }.split(",").map { it.toInt() }

        val newMap = (1..5).map { springMap }.joinToString("?")
        val newGroups = (1..5).flatMap { groups }

        val placeableHash = newGroups.sum() - newMap.count { it == '#' }
        val placeableDots = newMap.count { it == '?' } - placeableHash

        recurse(newMap, newGroups, placeableHash, placeableDots)
    }

    return newInput.sum()
}
