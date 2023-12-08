package year2021

import readLines

fun main() {
    val input = readLines("2021", "day3").map { it.split(("")).filter { bit -> bit == "0" || bit == "1" } }
    val testInput = readLines("2021", "day3_test").map { it.split(("")).filter { bit -> bit == "0" || bit == "1" } }

    check(part1(testInput) == 198)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 230)
    println("Part 2:" + part2(input))
}

private fun part1(input: List<List<String>>): Int {
    val bitsSize = input[0].size
    val gamma = mutableListOf<String>()
    val epsilon = mutableListOf<String>()

    for (bit in 0 until bitsSize) {
        if (mostCommonIsOne(input, bit)) {
            gamma.add("1")
            epsilon.add("0")
        } else {
            gamma.add("0")
            epsilon.add("1")
        }
    }

    return gamma.toBinary() * epsilon.toBinary()
}

private fun part2(input: List<List<String>>): Int {
    val bitsSize = input[0].size

    var oxygenGeneratorRating = input.map { ArrayList(it) }
    for (bit in 0 until bitsSize) {
        oxygenGeneratorRating = oxygenGeneratorRating.filter { (it[bit] == "1") == mostCommonIsOne(oxygenGeneratorRating, bit) }

        if (oxygenGeneratorRating.size == 1) break
    }

    var co2ScrubberRating = input.map { ArrayList(it) }
    for (bit in 0 until bitsSize) {
        co2ScrubberRating = co2ScrubberRating.filter { (it[bit] == "1") != mostCommonIsOne(co2ScrubberRating, bit) }

        if (co2ScrubberRating.size == 1) break
    }

    return oxygenGeneratorRating[0].toBinary() * co2ScrubberRating[0].toBinary()
}

private fun mostCommonIsOne(input: List<List<String>>, i: Int) = input.filter { it[i] == "1" }.size * 2 >= input.size

private fun List<String>.toBinary(): Int = this.joinToString("").toInt(2)
