package year2023

import readLines

fun main() {
    check(LongRange(98, 99).fastIntersect(LongRange(10, 200)).toList() == listOf(98L, 99L))
    check(LongRange(98, 99).fastIntersect(LongRange(100, 200)).isEmpty())
    check(LongRange(10, 20).fastIntersect(LongRange(15, 22)).toList() == listOf<Long>(15, 16, 17, 18, 19, 20))
    check(LongRange(10, 20).fastIntersect(LongRange(7, 16)).toList() == listOf<Long>(10, 11, 12, 13, 14, 15, 16))
    check(toLongRange(98, 2).addValue(-48).toList() == listOf<Long>(50, 51))

    val input = parseInput(readLines("2023", "day5"))
    val testInput = parseInput(readLines("2023", "day5_test"))

    val part1Result = part1(testInput)
    check(part1Result == 35L) { "Part 1 test case failed with result = $part1Result" }
    println("Part 1:" + part1(input))

    val part2Result = part2(testInput)
    check(part2Result == 46L) { "Part 2 test case failed with result = $part2Result" }
    println("Part 2:" + part2(input))
}

private fun parseInput(input: List<String>): Pair<List<Long>, List<Mapping>> {
    val seeds = input[0].dropWhile { !it.isDigit() }.split(" ").map { it.toLong() }
    val maps = mutableListOf<Mapping>()

    var input = input.drop(2)
    while (input.isNotEmpty()) {
        val mapLength = input.drop(1).takeWhile { it != "" }.size

        val mapInput = input.drop(1).take(mapLength).map { row -> row.split(" ").map { it.toLong() } }

        val mappings =
            mapInput.map { triple ->
                val destinationRangeStart = triple[0]
                val sourceRangeStart = triple[1]
                val rangeLength = triple[2]
                SingleMapping(toLongRange(sourceRangeStart, rangeLength), destinationRangeStart - sourceRangeStart)
            }
        maps.add(Mapping(mappings))

        input = input.drop(mapLength + 2)
    }

    return Pair(seeds, maps.toList())
}

private fun part1(input: Pair<List<Long>, List<Mapping>>): Long {
    var (seeds, maps) = input

    for (map in maps) {
        seeds =
            seeds.map { seed ->
                val shiftingValue = map.singleMappings.firstOrNull { singleMapping -> singleMapping.range.contains(seed) }?.value ?: 0
                seed + shiftingValue
            }
    }

    return seeds.min()
}

private fun part2(input: Pair<List<Long>, List<Mapping>>): Long {
    val (rawSeeds, maps) = input

    var seedRanges: List<LongRange> =
        rawSeeds.mapIndexed { index, long -> Pair(index, long) }.groupBy { it.first / 2 }.map { entry ->
            val numberPair = entry.value.map { it.second }
            toLongRange(numberPair[0], numberPair[1])
        }

    for (map in maps) {
        seedRanges =
            seedRanges.map { seedRange ->
                val newRangesWithMappingIntersections =
                    map.singleMappings.map {
                            singleMapping ->
                        singleMapping.range.fastIntersect(seedRange).addValue(singleMapping.value)
                    }
//            val newRangeWithoutAnyIntersection = map.singleMappings.map { singleMapping -> singleMapping.range. }
                newRangesWithMappingIntersections
            }.flatten().filter { !it.isEmpty() }
    }

    return 0L
}

private fun LongRange.addValue(value: Long) = LongRange(start + value, endInclusive + value)

private fun LongRange.fastIntersect(other: LongRange): LongRange {
    return LongRange(this.first.coerceAtLeast(other.first), this.last.coerceAtMost(other.last))
}

private fun toLongRange(
    start: Long,
    length: Long,
) = LongRange(start, start + length - 1)

private data class Mapping(val singleMappings: List<SingleMapping>)

private data class SingleMapping(val range: LongRange, val value: Long)
