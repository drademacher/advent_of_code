package year2023

import readLines
import kotlin.random.Random

fun main() {
    val input = parseInput(readLines("2023", "day6"))
    val testInput = parseInput(readLines("2023", "day6_test"))

    check(part1(testInput) == 288)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 71503)
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): List<String> {
    return input
}

private fun part1(input: List<String>): Int {
    val times = input[0].dropWhile { !it.isDigit() }.split(" ").filter { it != "" }.map { it.toInt() }
    val distancesToBeat = input[1].dropWhile { !it.isDigit() }.split(" ").filter { it != "" }.map { it.toInt() }

    var result = 1
    for (i in times.indices) {
        val time = times[i]
        val distanceToBeat = distancesToBeat[i]

        val distances =
            (1..time)
                .map { x -> (time - x) * x }
                .filter { distance -> distance > distanceToBeat }
                .size

        result *= distances
    }

    return result
}

private fun part2(input: List<String>): Int {
    val time = input[0].filter { it.isDigit() }.toLong()
    val distanceToBeat = input[1].filter { it.isDigit() }.toLong()

    return distancesOverThreshold(time, distanceToBeat).toInt()
}

private fun distancesOverThreshold(
    time: Long,
    distanceThreshold: Long,
): Long {
    fun distance(x: Long) = (time - x) * x

    fun distanceOverThreshold(x: Long) = distance(x) >= distanceThreshold

    fun getRandomTimeOverThreshold(): Long {
        var result: Long
        val random = Random(0)

        do {
            result = random.nextLong(time - 1)
        } while (distance(result) < distanceThreshold)

        return result
    }

    fun binarySearchForBoundaryTime(
        left: Long,
        right: Long,
    ): Long {
        if (right - left < 50L) {
            return if (distanceOverThreshold(right)) {
                (left..right).first { distanceOverThreshold(it) }
            } else {
                (left..right).last { distanceOverThreshold(it) }
            }
        }

        val mid: Long = (left + right) / 2

        return if (distanceOverThreshold(mid) != distanceOverThreshold(left)) {
            binarySearchForBoundaryTime(left, mid - 1)
        } else {
            binarySearchForBoundaryTime(mid + 1, right)
        }
    }

    val splitNumber = getRandomTimeOverThreshold()

    return binarySearchForBoundaryTime(splitNumber, time - 1) - binarySearchForBoundaryTime(0, splitNumber) + 1
}
