package year2021

import readLines

fun main() {
    val input = readLines("2021", "day10")
    val testInput = readLines("2021", "day10_test")

    check(part1(testInput) == 26397)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 288957L)
    println("Part 2:" + part2(input))
}

private fun part1(input: List<String>): Int {
    val evaluateError = hashMapOf(")" to 3, "]" to 57, "}" to 1197, ">" to 25137)
    var result = 0

    for (line in input) {
        val chars = line.split("").filter { it != "" }
        val stack = mutableListOf<String>()
        for (char in chars) {
            if (isChunkOpening(char)) {
                stack.add(char)
                continue
            }

            if (stack.isEmpty()) {
                print("Input invalid, too many closing chunks")
                break
            }

            val previous = stack.removeLast()
            if (!isCorresponding(previous, char)) {
                result += evaluateError[char]!!
            }
        }
    }

    return result
}

private fun part2(input: List<String>): Long {
    val evaluateError = hashMapOf("(" to 1, "[" to 2, "{" to 3, "<" to 4)

    val lineScores = mutableListOf<Long>()
    for (line in input) {
        val chars = line.split("").filter { it != "" }
        val stack = mutableListOf<String>()
        for (char in chars) {
            if (isChunkOpening(char)) {
                stack.add(char)
                continue
            }

            if (stack.isEmpty()) {
                print("Input invalid, too many closing chunks")
                break
            }

            val previous = stack.removeLast()
            if (!isCorresponding(previous, char)) {
                stack.clear()
                break
            }
        }

        if (stack.isNotEmpty()) {
            val lineScore = stack.foldRight(0L) { char, acc -> acc * 5 + evaluateError[char]!! }
            lineScores.add(lineScore)
        }
    }

    return lineScores.sorted()[lineScores.size / 2]
}

private fun isChunkOpening(c: String): Boolean = setOf("(", "[", "{", "<").contains(c)

private fun isCorresponding(
    start: String,
    end: String,
) = listOf("(", "[", "{", "<").indexOf(start) == listOf(")", "]", "}", ">").indexOf(end)
