package year2021

import Point
import readLines

fun main() {
    val input = parseInput(readLines("2021", "day9"))
    val testInput = parseInput(readLines("2021", "day9_test"))

    check(part1(testInput) == 15)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 1134)
    println("Part 2:" + part2(input))
}

private fun parseInput(lines: List<String>): Day9Input {
    return lines
        .map { line -> line.split("").filter { it != "" }.map { it.toInt() } }
}

private fun part1(input: Day9Input): Int {
    return determineLowPoints(input).sumOf { input[it.y][it.x] + 1 }
}

private fun determineLowPoints(input: Day9Input): Set<Point> {
    val lowPoints = mutableSetOf<Point>()
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (getOrthogonalNeighbors(input, Point(x, y)).all { input[it.y][it.x] > input[y][x] }) {
                lowPoints.add(Point(x, y))
            }
        }
    }
    return lowPoints
}

private fun getOrthogonalNeighbors(
    input: Day9Input,
    point: Point,
): Set<Point> {
    val result = mutableSetOf<Point>()

    if (point.x > 0) result.add(Point(point.x - 1, point.y))
    if (point.x < input[point.y].size - 1) result.add(Point(point.x + 1, point.y))
    if (point.y > 0) result.add(Point(point.x, point.y - 1))
    if (point.y < input.size - 1) result.add(Point(point.x, point.y + 1))

    return result
}

private fun part2(input: Day9Input): Int {
    return determineLowPoints(input)
        .map { lowPoint -> findBasin(input, lowPoint) }
        .map { it.size }
        .sorted()
        .reversed()
        .take(3)
        .reduce { a, b -> a * b }
}

private fun findBasin(
    input: Day9Input,
    start: Point,
): Set<Point> {
    val frontier = mutableListOf(start)
    val visited = mutableSetOf<Point>()

    while (frontier.size > 0) {
        val next = frontier.removeFirst()
        if (visited.contains(next) || input[next.y][next.x] == 9) {
            continue
        }

        visited.add(next)
        val neighbors = getOrthogonalNeighbors(input, next)
        frontier.addAll(neighbors)
    }

    return visited
}

private typealias Day9Input = List<List<Int>>
