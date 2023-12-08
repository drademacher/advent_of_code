package year2021

import readFile

fun main() {
    val input = parseInput(readFile("2021", "day4"))
    val testInput = parseInput(readFile("2021", "day4_test"))

    check(part1(testInput) == 4512)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 1924)
    println("Part 2:" + part2(input))
}

private fun parseInput(file: String): NumbersAndBoards {
    val rawInput =
        file
            .split("\n")
            .filter { it != "" }
    val numbers = rawInput[0].split(",").map { it.toInt() }
    val boards =
        (1 until rawInput.size step 5)
            .map { lineIndex ->
                Board(
                    rawInput
                        .slice(lineIndex..lineIndex + 4)
                        .map { line -> line.split(" ").filter { it != "" }.map { it.toInt() } },
                )
            }
    return NumbersAndBoards(numbers, boards)
}

private fun part1(input: NumbersAndBoards): Int {
    val (numbers, boards) = input

    for (number in numbers) {
        boards.forEach { it.mark(number) }

        if (boards.any { it.hasBingo() }) {
            val board = boards.first { it.hasBingo() }
            return board.sumOfUnmarkedNumbers() * number
        }
    }

    return -1
}

private fun part2(input: NumbersAndBoards): Int {
    val (numbers, boards) = input

    var lastBoard = Board(mutableListOf())
    for (number in numbers) {
        boards.forEach { it.mark(number) }

        if (boards.filter { !it.hasBingo() }.size == 1) {
            lastBoard = boards.first { !it.hasBingo() }
            break
        }
    }

    // solve the last board
    for (number in numbers) {
        lastBoard.mark(number)

        if (lastBoard.hasBingo()) {
            return lastBoard.sumOfUnmarkedNumbers() * number
        }
    }

    return -1
}

data class NumbersAndBoards(val numbers: List<Int>, val boards: List<Board>)

class Board(val numbers: List<List<Int>>) {
    var marked: MutableList<MutableList<Boolean>> = mutableListOf()

    init {
        for (i in numbers.indices) {
            marked.add(Array(5) { false }.toMutableList())
        }
    }

    fun mark(number: Int) {
        for (i in marked.indices) {
            for (j in marked.indices) {
                if (numbers[i][j] == number) {
                    marked[i][j] = true
                }
            }
        }
    }

    fun hasBingo(): Boolean {
        for (i in marked.indices) {
            val lineMatch = marked.indices.all { j -> marked[i][j] }
            val rowMatch = marked.indices.all { j -> marked[j][i] }
            val firstDiagonal = marked.indices.map { marked.size - 1 - it }.all { j -> marked[i][j] }
            val secondDiagonal = marked.indices.map { marked.size - 1 - it }.all { j -> marked[j][i] }

            if (lineMatch || rowMatch || firstDiagonal || secondDiagonal) {
                return true
            }
        }

        return false
    }

    fun sumOfUnmarkedNumbers(): Int {
        var result = 0
        for (i in marked.indices) {
            for (j in marked.indices) {
                if (!marked[i][j]) {
                    result += numbers[i][j]
                }
            }
        }
        return result
    }
}
