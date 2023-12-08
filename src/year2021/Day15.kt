package year2021

import Point
import readLines
import java.util.PriorityQueue

fun main() {
    val input = parseInput(readLines("2021", "day15"))
    val testInput = parseInput(readLines("2021", "day15_test"))

    check(part1(testInput) == 40)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 315)
    println("Part 2:" + part2(input))
}

private fun parseInput(lines: List<String>): Day15Input = lines
    .map { it.split("").filter { it != "" }.map { it.toInt() } }

private fun part1(input: List<List<Int>>): Int? {
    return searchCheapestPathToBottomRIght(input)
}

private fun part2(input: List<List<Int>>): Int? {
    val yMax = input.size
    val xMax = input[0].size
    val newInput = MutableList(5 * yMax) { MutableList(5 * xMax) { 0 } }

    for (y in 0 until yMax * 5) {
        for (x in 0 until xMax * 5) {
            val incrementedWithOverflow = input[y % yMax][x % xMax] + (y / yMax) + (x / xMax)
            newInput[y][x] = if (incrementedWithOverflow > 9) incrementedWithOverflow - 9 else incrementedWithOverflow
        }
    }

    return searchCheapestPathToBottomRIght(newInput)
}

private fun searchCheapestPathToBottomRIght(input: List<List<Int>>): Int? {
    val goal = Point(input.first().size - 1, input.size - 1)
    val frontier = PriorityQueue<Pair<Point, Int>> { a, b -> a.second - b.second }
    val visited = mutableSetOf<Point>()

    frontier.add(Pair(Point(0, 0), 0))
    while (frontier.isNotEmpty()) {
        val next = frontier.poll()

        if (visited.contains(next.first)) {
            continue
        }

        if (next.first == goal) {
            return next.second
        }

        visited.add(next.first)
        getOrthogonalNeighbors(input, next.first)
            .map { Pair(it, next.second + input[it.y][it.x]) }
            .forEach(frontier::add)
    }

    return null
}

private fun getOrthogonalNeighbors(input: Day15Input, point: Point): Set<Point> {
    val result = mutableSetOf<Point>()

    if (point.x > 0) result.add(Point(point.x - 1, point.y))
    if (point.x < input[point.y].size - 1) result.add(Point(point.x + 1, point.y))
    if (point.y > 0) result.add(Point(point.x, point.y - 1))
    if (point.y < input.size - 1) result.add(Point(point.x, point.y + 1))

    return result
}

private typealias Day15Input = List<List<Int>>

fun List<List<Int>>.get(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)
