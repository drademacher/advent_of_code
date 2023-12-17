fun main() {
    val input = parseInput(readLines("2023", "day"))
    val testInput = parseInput(readLines("2023", "day_test"))

    val testResultPart1 = part1(testInput)
    check(testResultPart1 == 0) { "Incorrect result for part 1: $testResultPart1" }
    println("Part 1:" + part1(input))

    val testResultPart2 = part2(testInput)
    check(testResultPart2 == 0) { "Incorrect result for part 2: $testResultPart2" }
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
