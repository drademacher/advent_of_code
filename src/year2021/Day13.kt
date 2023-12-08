package year2021

import Point
import readFile

fun main() {
    val input = parseInput(readFile("2021", "day13"))
    val testInput = parseInput(readFile("2021", "day13_test"))

    check(part1(testInput) == 17)
    println("Part 1:" + part1(input))

    println("Part 2:")
    val (width, height) = part2(input)
    input.paper.print(width, height)
}

private fun parseInput(readText: String): Day13Input {
    val (rawPoints, rawFolds) = readText.split("\n\n")

    val points =
        rawPoints
            .split("\n")
            .filter { it != "" }
            .map { it.split(",").map { it.toInt() } }
            .map { Point(it[0], it[1]) }

    val foldInstructions =
        rawFolds
            .split("\n")
            .filter { it != "" }
            .map { it.split(" ").last().split("=") }
            .map { FoldInstruction(Axis.valueOf(it[0].uppercase()), it[1].toInt()) }

    return Day13Input(Paper.fromPoints(points), foldInstructions)
}

private fun part1(input: Day13Input): Int {
    input.paper.fold(input.foldInstructions.first())
    return input.paper.dots()
}

private fun part2(input: Day13Input): Pair<Int, Int> {
    input.foldInstructions.forEach { foldInstruction -> input.paper.fold(foldInstruction) }
    val width = input.foldInstructions.filter { it.axis == Axis.X }.minOf { it.at }
    val height = input.foldInstructions.filter { it.axis == Axis.Y }.minOf { it.at }
    return Pair(width, height)
}

data class Day13Input(val paper: Paper, val foldInstructions: List<FoldInstruction>)

data class FoldInstruction(val axis: Axis, val at: Int)

enum class Axis { X, Y }

data class Paper(val dots: Array<Array<Boolean>>) {
    fun fold(foldInstruction: FoldInstruction) {
        when (foldInstruction.axis) {
            Axis.X -> foldVertically(foldInstruction.at)
            Axis.Y -> foldHorizontally(foldInstruction.at)
        }
    }

    private fun foldHorizontally(at: Int) {
        for (y in 0..at) {
            for (x in dots[0].indices) {
                dots[y][x] = dots[y][x] || dots[at + at - y][x]
            }
        }

        for (y in at + 1 until dots.size) {
            for (x in dots[y].indices) {
                dots[y][x] = false
            }
        }
    }

    private fun foldVertically(at: Int) {
        for (y in dots.indices) {
            for (x in 0..at) {
                dots[y][x] = dots[y][x] || dots[y][at + at - x]
            }
        }

        for (y in dots.indices) {
            for (x in at + 1 until dots[y].size) {
                dots[y][x] = false
            }
        }
    }

    fun dots(): Int = dots.sumOf { it.count { it } }

    fun print(
        x: Int = dots[0].size,
        y: Int = dots.size,
    ) {
        dots
            .take(y)
            .map { it.take(x).map { if (it) "█" else " " }.joinToString("") }
            .forEach(::println)
    }

    companion object {
        fun fromPoints(points: List<Point>): Paper {
            val maxY = points.maxOf { it.y } + 1
            val maxX = points.maxOf { it.x } + 1

            val paper = Paper(Array(maxY) { Array(maxX) { false } })
            points.forEach { paper.dots[it.y][it.x] = true }
            return paper
        }
    }
}
