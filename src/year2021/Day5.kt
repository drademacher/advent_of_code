package year2021

import readFile
import java.awt.Point

private const val COORDINATE_SIZE = 1000

fun main() {
    val input = parseInput(readFile("2021", "day5"))
    val testInput = parseInput(readFile("2021", "day5_test"))

    println(part1(testInput))
    check(part1(testInput) == 5)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 12)
    println("Part 2:" + part2(input))
}

private fun parseInput(file: String): List<Line> {
    return file
        .replace(" -> ", ",")
        .split("\n")
        .filter { it != "" }
        .map { it.split(",").map { it.toInt() } }
        .map { Line(Point(it[0], it[1]), Point(it[2], it[3])) }
}

private fun part1(lines: List<Line>): Int {
    val coords = Array(COORDINATE_SIZE) { IntArray(COORDINATE_SIZE) { 0 } }

    lines
        .filter { it.isHorizontalOrVertical }
        .flatMap { it.points }
        .forEach { point -> coords[point.y][point.x] += 1 }

    return coords.sumOf { col -> col.filter { it >= 2 }.size }
}

private fun part2(lines: List<Line>): Int {
    val coords = Array(COORDINATE_SIZE) { IntArray(COORDINATE_SIZE) { 0 } }

    lines
        .flatMap { it.points }
        .forEach { point -> coords[point.y][point.x] += 1 }

    return coords.sumOf { col -> col.filter { it >= 2 }.size }
}

class Line(private val start: Point, private val end: Point) {
    val isHorizontalOrVertical: Boolean = start.x == end.x || start.y == end.y
    val points: List<Point>
        get() {
            val xs =
                if (start.x < end.x) {
                    (start.x..end.x)
                } else {
                    (start.x downTo end.x)
                }
            val ys =
                if (start.y < end.y) {
                    (start.y..end.y)
                } else {
                    (start.y downTo end.y)
                }

            if (start.x == end.x) {
                return ys.map { Point(start.x, it) }
            }

            if (start.y == end.y) {
                return xs.map { Point(it, start.y) }
            }

            return xs.zip(ys).map { Point(it.first, it.second) }
        }
}
