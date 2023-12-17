package year2023

import readLines

fun main() {
    check(hash("H") == 200)
    check(hash("HA") == 153)
    check(hash("HAS") == 172)
    check(hash("HASH") == 52)

    val input = parseInput(readLines("2023", "day15"))
    val testInput = parseInput(readLines("2023", "day15_test"))

    check(part1(testInput) == 1320) { "Incorrect result for part 1: ${part1(testInput)}" }
    println("Part 1:" + part1(input))

    check(part2(testInput) == 145) { "Incorrect result for part 2: ${part2(testInput)}" }
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): List<String> {
    return input.first().split(",")
}

private fun part1(input: List<String>): Int {
    return input.sumOf { hash(it) }
}

private fun part2(inputs: List<String>): Int {
    val steps: List<Step> = inputs.map { input ->
        val label = input.takeWhile { it.isLetter() }
        val operation = input.find { it == '-' || it == '=' }.toString()

        if (operation == "=") {
            val focalLength = input.dropWhile { !it.isDigit() }.toInt()
            Step.Value(label, focalLength)
        } else {
            Step.Remove(label)
        }
    }

    val boxes = mutableListOf<MutableList<Step.Value>>()
    repeat(256) { boxes.add(mutableListOf()) }

    for (step in steps) {
        when (step) {
            is Step.Value -> {
                val hash = hash(step.label)
                val sameLabeledLens = boxes[hash].indexOfFirst { it.label == step.label }
                if (sameLabeledLens == -1) {
                    boxes[hash].add(step)
                } else {
                    boxes[hash] = boxes[hash].map {
                        if (it.label == step.label) {
                            step
                        } else {
                            it
                        }
                    }.toMutableList()
                }
            }

            is Step.Remove -> {
                boxes[hash(step.label)].removeAll { it.label == step.label }
            }
        }
    }

    return boxes
        .withIndex()
        .sumOf { (boxNumber, boxContent) ->
            boxContent
                .withIndex()
                .map { (slot, lens) -> (slot + 1) * lens.focalLength }
                .sumOf { it * (boxNumber + 1) }
        }
}

private fun hash(str: String): Int {
    var result = 0

    for (char in str.chars()) {
        result += char
        result *= 17
        result %= 256
    }

    return result
}

private sealed interface Step {
    data class Value(val label: String, val focalLength: Int) : Step
    data class Remove(val label: String) : Step
}
