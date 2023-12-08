package year2021

import readLines

fun main() {
    val input = readLines("2021", "day2").map { Movement.fromString(it) }
    val testInput = readLines("2021", "day2_test").map { Movement.fromString(it) }

    check(part1(testInput) == 150)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 900)
    println("Part 2:" + part2(input))
}

private fun part1(input: List<Movement>): Int {
    var horizontal = 0
    var depth = 0
    for (movement in input) {
        when (movement) {
            is Movement.Forward -> horizontal += movement.number
            is Movement.Up -> depth -= movement.number
            is Movement.Down -> depth += movement.number
        }
    }
    return horizontal * depth
}

private fun part2(input: List<Movement>): Int {
    var horizontal = 0
    var aim = 0
    var depth = 0
    for (movement in input) {
        when (movement) {
            is Movement.Forward -> {
                horizontal += movement.number
                depth += aim * movement.number
            }
            is Movement.Up -> aim -= movement.number
            is Movement.Down -> aim += movement.number
        }
    }
    return horizontal * depth
}

sealed class Movement {
    data class Forward(val number: Int) : Movement()
    data class Up(val number: Int) : Movement()
    data class Down(val number: Int) : Movement()

    companion object {
        fun fromString(string: String): Movement {
            val parts = string.split(" ")

            return when (parts[0]) {
                "forward" -> Forward(parts[1].toInt())
                "up" -> Up(parts[1].toInt())
                "down" -> Down(parts[1].toInt())
                else -> throw RuntimeException("should not happen: '${string}'")
            }
        }
    }
}
