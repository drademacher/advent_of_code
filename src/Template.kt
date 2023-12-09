fun main() {
    val input = parseInput(readLines("2023", "day"))
    val testInput = parseInput(readLines("2023", "day_test"))

    check(part1(testInput) == 0)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 0)
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): List<String> {
    return input
}

private fun part1(input: List<String>): Int {
    return 0
}

private fun part2(input: List<String>): Int {
    return 0
}
