package year2023

import readLines

fun main() {
    val input = readLines("2023", "day1")
    val testInputPart1 = readLines("2023", "day1_test1")
    val testInputPart2 = readLines("2023", "day1_test2")

    check(part1(testInputPart1) == 142)
    println("Part 1:" + part1(input))

    check(part2(testInputPart2) == 281)
    println("Part 2:" + part2(input))
}

private fun part1(input: List<String>): Int {
    return input.sumOf(::extractFirstAndLastDigit)
}

private fun extractFirstAndLastDigit(row: String): Int {
    return ("" + row.first { it.isDigit() } + row.last { it.isDigit() }).toInt()
}

private fun part2(input: List<String>): Int {
    return input.sumOf(::extractFirstAndLastNumber)
}

private fun extractFirstAndLastNumber(row: String): Int {
    val numbersAsWords =
        listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    val leftNumberAsAWord = row.takeWhile { it.isLetter() }.findAnyOf(numbersAsWords)?.second
    val left =
        if (leftNumberAsAWord != null) {
            numbersAsWords.indexOf(leftNumberAsAWord) + 1
        } else {
            row.first { it.isDigit() }.digitToInt()
        }

    val rightNumberAsAWord =
        row.takeLastWhile { it.isLetter() }.findLastAnyOf(numbersAsWords)?.second
    val right =
        if (rightNumberAsAWord != null) {
            numbersAsWords.indexOf(rightNumberAsAWord) + 1
        } else {
            row.last { it.isDigit() }.digitToInt()
        }

    return "$left$right".toInt()
}
