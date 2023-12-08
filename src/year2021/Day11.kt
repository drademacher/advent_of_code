package year2021

import Point
import readLines

fun main() {
    val input = parseInput(readLines("2021", "day11"))
    val testInput = parseInput(readLines("2021", "day11_test"))

    check(part1(testInput.clone()) == 1656)
    println("Part 1:" + part1(input.clone()))

    check(part2(testInput.clone()) == 195)
    println("Part 2:" + part2(input.clone()))
}

private fun parseInput(lines: List<String>): Day11Input {
    return Day11Input(
        lines
            .map { it.split("").filter { it != "" }.map { it.toInt() }.toMutableList() }
            .toMutableList()
    )
}

private fun part1(input: Day11Input): Int {
    var totalFlashes = 0
    repeat(100) {
        input.getAllPoints().forEach {
            input.grid[it.y][it.x] += 1
        }

        val hasFlashed = mutableSetOf<Point>()
        val flashingNeighbors = input.getAllPoints()
            .filter { input.getPoint(it)!! == 10 }
            .toMutableList()


        while (flashingNeighbors.isNotEmpty()) {
            val next = flashingNeighbors.removeFirst()
            if (hasFlashed.contains(next)) {
                continue
            }
            hasFlashed.add(next)
            incrementNeighbors(input, next)
            flashingNeighbors.addAll(getFlashingNeighbors(input, next))
        }

        totalFlashes += hasFlashed.size
        hasFlashed.forEach {
            input.grid[it.y][it.x] = 0
        }
    }

    return totalFlashes
}

private fun part2(input: Day11Input): Int {
    for (day in 1..Int.MAX_VALUE) {
        for (y in input.grid.indices) {
            for (x in input.grid[y].indices) {
                input.grid[y][x] += 1
            }
        }

        val hasFlashed = mutableSetOf<Point>()
        val flashingNeighbors = input.grid.indices.flatMap { y -> input.grid[y].indices.map { Point(y, it) } }
            .filter { input.getPoint(it)!! == 10 }
            .toMutableList()


        while (flashingNeighbors.isNotEmpty()) {
            val next = flashingNeighbors.removeFirst()
            if (hasFlashed.contains(next)) {
                continue
            }
            hasFlashed.add(next)
            incrementNeighbors(input, next)
            flashingNeighbors.addAll(getFlashingNeighbors(input, next))
        }

        hasFlashed.forEach {
            input.grid[it.y][it.x] = 0
        }

        if (hasFlashed.size == input.grid.size * input.grid[0].size) {
            return day
        }
    }

    return -1
}

private fun getNeighbors(input: Day11Input, point: Point): List<Point> = (-1..+1).flatMap { x -> (-1..+1).map { y -> Point(x, y) } }
    .filter { it != Point(0, 0) }
    .map { it.add(point) }
    .filter { input.getOrNull(it.y)?.getOrNull(it.x) != null }

private fun getFlashingNeighbors(input: Day11Input, point: Point) = getNeighbors(input, point)
    .filter { input.getPoint(it) == 10 }

private fun incrementNeighbors(input: Day11Input, point: Point) {
    getNeighbors(input, point)
        .forEach {
            input.grid[it.y][it.x] += 1
        }
}

data class Day11Input(val grid: MutableList<MutableList<Int>>) {
    fun print() = grid.forEach { println(it.joinToString("")) }
    fun getPoint(point: Point): Int? = grid.getOrNull(point.y)?.getOrNull(point.x)
    fun getAllPoints() = grid.indices.flatMap { y -> grid[y].indices.map { x -> Point(y, x) } }
    fun clone(): Day11Input = Day11Input(grid.map { it.toMutableList() }.toMutableList())
    fun getOrNull(y: Int) = grid.getOrNull(y)
}


