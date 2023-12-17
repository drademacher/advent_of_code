package year2023

import Direction
import MutableGrid
import readLines

fun main() {
    val input = parseInput(readLines("2023", "day14"))
    val testInput = parseInput(readLines("2023", "day14_test"))

    val testResultPart1 = part1(testInput.simpleCopy())
    check(testResultPart1 == 136) { "Incorrect result for part 1: $testResultPart1" }
    println("Part 1:" + part1(input))

    val testResultPart2 = part2(testInput.simpleCopy())
    check(testResultPart2 == 64) { "Incorrect result for part 2: $testResultPart2" }
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): MutableGrid<Char> {
    return MutableGrid(input.map { it.toCharArray().toMutableList() }.toMutableList())
}

private fun part1(grid: MutableGrid<Char>): Int {
    grid.tilt(Direction.NORTH)
    return grid.calculateTotalLoad()
}

private fun part2(grid: MutableGrid<Char>): Int {
    val totalCycles = 1_000_000_000
    val gridStateCache = mutableMapOf<String, Int>()

    for (cycle in (0..<totalCycles)) {
        val stateInPreviousCycleReached = gridStateCache[grid.toCacheableString()]

        if (stateInPreviousCycleReached != null) {
            val cycleLength = cycle - stateInPreviousCycleReached
            val remainingCycles = (totalCycles - stateInPreviousCycleReached) % cycleLength

            repeat(remainingCycles) {
                grid.executeTiltCycle()
            }

            return grid.calculateTotalLoad()
        } else {
            gridStateCache[grid.toCacheableString()] = cycle
        }

        grid.executeTiltCycle()
    }

    return -1
}

private fun MutableGrid<Char>.executeTiltCycle() {
    tilt(Direction.NORTH)
    tilt(Direction.WEST)
    tilt(Direction.SOUTH)
    tilt(Direction.EAST)
}

private fun MutableGrid<Char>.calculateTotalLoad(): Int {
    return data
        .withIndex()
        .sumOf { (rowIndex, row) -> row.count { it == 'O' } * (rows - rowIndex) }
}

private fun MutableGrid<Char>.tilt(direction: Direction) {
    when (direction) {
        Direction.NORTH -> {
            for (x in data[0].indices) {
                var obstacle = -1

                for (y in (0..<rows)) {
                    if (data[y][x] == '#') {
                        obstacle = y
                    } else if (data[y][x] == 'O') {
                        obstacle++
                        data[y][x] = '.'
                        data[obstacle][x] = 'O'
                    }
                }
            }
        }

        Direction.EAST -> {
            for (y in (0..<rows)) {
                var obstacle = cols

                for (x in (0..<cols).reversed()) {
                    if (data[y][x] == '#') {
                        obstacle = x
                    } else if (data[y][x] == 'O') {
                        obstacle--
                        data[y][x] = '.'
                        data[y][obstacle] = 'O'
                    }
                }
            }
        }

        Direction.SOUTH -> {
            for (x in (0..<cols)) {
                var obstacle = rows

                for (y in (0..<rows).reversed()) {
                    if (data[y][x] == '#') {
                        obstacle = y
                    } else if (data[y][x] == 'O') {
                        obstacle--
                        data[y][x] = '.'
                        data[obstacle][x] = 'O'
                    }
                }
            }
        }

        Direction.WEST -> {
            for (y in (0..<rows)) {
                var obstacle = -1

                for (x in (0..<cols)) {
                    if (data[y][x] == '#') {
                        obstacle = x
                    } else if (data[y][x] == 'O') {
                        obstacle++
                        data[y][x] = '.'
                        data[y][obstacle] = 'O'
                    }
                }
            }
        }
    }
}
