package year2023

import Grid
import readLines

fun main() {
    val input = parseInput(readLines("2023", "day13"))
    val testInput = parseInput(readLines("2023", "day13_test"))


    check(part1(testInput) == 405)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 400)
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): List<Grid<Char>> {
    var input = input

    val patterns = mutableListOf<Grid<Char>>()
    while (input.isNotEmpty()) {
        val patternLength = input.takeWhile { it != "" }.size
        patterns.add(Grid(input.take(patternLength).map { it.toCharArray().toList() }))
        input = input.drop(patternLength + 1)
    }

    return patterns
}

private fun part1(patterns: List<Grid<Char>>): Int {
    val x = patterns.map { pattern -> (isHorizontallyMirrored(pattern).firstOrNull()?.let { it * 100 }) ?: isVerticallyMirrored(pattern).first() }

    return x.sum()
}

private fun part2(patterns: List<Grid<Char>>): Int {
    return patterns.map { pattern ->

        val initialHorizontalMirrors = isHorizontallyMirrored(pattern).toSet()
        val initialVerticalMirrors = isVerticallyMirrored(pattern).toSet()

        pattern.allFlips().mapNotNull { flippedPattern ->
            val horizontalMirrors = isHorizontallyMirrored(flippedPattern).minus(initialHorizontalMirrors)
            val verticalMirrors = isVerticallyMirrored(flippedPattern).minus(initialVerticalMirrors)

            if (horizontalMirrors.isNotEmpty() || verticalMirrors.isNotEmpty()) {
                horizontalMirrors.firstOrNull()?.let { it * 100 } ?: verticalMirrors.first()
            } else {
                null
            }
        }.first()
    }.sum()


    return 0
}

private fun isHorizontallyMirrored(pattern: Grid<Char>): List<Int> {
    return (1..<pattern.rows).filter { mirror ->
        (1..mirror).all { row ->
            val mirroredRow = 2 * mirror - row + 1
            mirroredRow > pattern.rows || pattern.nthRow(row) == pattern.nthRow(mirroredRow)
        }
    }
}

private fun isVerticallyMirrored(pattern: Grid<Char>): List<Int> {
    return (1..<pattern.cols).filter { mirror ->
        (1..mirror).all { col ->
            val mirroredCol = 2 * mirror - col + 1
            mirroredCol > pattern.cols || pattern.nthCol(col) == pattern.nthCol(mirroredCol)
        }
    }
}

private fun Grid<Char>.allFlips(): List<Grid<Char>> {

    return (1..rows).map { row ->
        (1..cols).map { col ->
            this.flip(row, col)
        }
    }.flatten()
}

private fun Grid<Char>.flip(col: Int, row: Int): Grid<Char> {
    val newData = data.mapIndexed { y, chars ->
        if (y == row - 1) {
            chars.mapIndexed { x, char ->
                if (x == col - 1) {
                    if (char == '.') '#' else '.'
                } else {
                    char
                }
            }
        } else {
            chars
        }
    }

    return Grid(newData)
}
