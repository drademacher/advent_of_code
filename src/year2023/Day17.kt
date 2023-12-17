package year2023

import Grid
import Point
import readLines
import java.util.PriorityQueue

// TODO: very slow solution for no obvious reason
fun main() {
    val input = parseInput(readLines("2023", "day17"))
    val testInput = parseInput(readLines("2023", "day17_test"))

    check(part1(testInput) == 102) { "Incorrect result for part 1: ${part1(testInput)}" }
    println("Part 1:" + part1(input))

    check(part2(testInput) == 94) { "Incorrect result for part 2: ${part2(testInput)}" }
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): Grid<Int> {
    return Grid(input.map { line -> line.toCharArray().toList().map { it.digitToInt() } })
}

private fun part1(grid: Grid<Int>): Int {
    return solve(grid, 1, 3)
}

private fun part2(grid: Grid<Int>): Int {
    return solve(grid, 4, 10)
}

private fun solve(grid: Grid<Int>, minStraightMoves: Int, maxStraightMoves: Int): Int {
    val goal = Point(grid.cols - 1, grid.rows - 1)

    val visited = mutableListOf<Pair<Point, Direction>>()
    val frontier = PriorityQueue<State>(compareBy<State> { it.costSoFar }.thenBy { it.point.manhattenDistance(goal) }).also { queue ->
        queue.addAll(Direction.entries.map { State(Point(0, 0), it, 0) })
    }

    while (frontier.isNotEmpty()) {
        val state = frontier.remove()
        val point = state.point

        if (point == goal) {
            return state.costSoFar
        }

        if (visited.contains(Pair(state.point, state.fromDirection))) {
            continue
        }

        visited.add(Pair(state.point, state.fromDirection))
        frontier.addAll(state.generateAllValidNextStates(grid, minStraightMoves, maxStraightMoves))
    }

    return -1
}

private data class State(val point: Point, val fromDirection: Direction, val costSoFar: Int) {
    fun generateAllValidNextStates(grid: Grid<Int>, minStraightMoves: Int, maxStraightMoves: Int): List<State> {
        val neighbors = mutableListOf<State>()

        for (direction in fromDirection.ninetyDegree()) {
            var newCost = 0
            var newPoint = point
            for (amount in (1..maxStraightMoves)) {
                newPoint = newPoint.add(direction.toPoint())
                newCost += grid.getOrNull(newPoint.x, newPoint.y) ?: 0
                if (amount >= minStraightMoves) {
                    neighbors.add(State(newPoint, direction, costSoFar + newCost))
                }
            }
        }

        return neighbors.filter { it.point.x >= 0 && it.point.x < grid.cols && it.point.y >= 0 && it.point.y < grid.rows }
    }
}

private enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    ;

    fun ninetyDegree() = when (this) {
        NORTH, SOUTH -> listOf(EAST, WEST)
        EAST, WEST -> listOf(NORTH, SOUTH)
    }

    fun toPoint() =
        when (this) {
            NORTH -> Point(0, -1)
            EAST -> Point(1, 0)
            SOUTH -> Point(0, 1)
            WEST -> Point(-1, 0)
        }
}
