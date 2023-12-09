package year2023

import readLines

fun main() {
    val input = parseInput(readLines("2023", "day7"))
    val testInput = parseInput(readLines("2023", "day7_test"))

    check(CamelCards("T55J5", 0).cardType(false) == 4)
    check(CamelCards("T55J5", 0).cardType(true) == 6)
    check(CamelCards("KTJJT", 0).cardType(false) == 3)
    check(CamelCards("KTJJT", 0).cardType(true) == 6)
    check(CamelCards("QQQJA", 0).cardType(false) == 4)
    check(CamelCards("QQQJA", 0).cardType(true) == 6)

    check(part1(testInput) == 6440)
    println("Part 1:" + part1(input))

    check(part2(testInput) == 5905)
    println("Part 2:" + part2(input))
}

private fun parseInput(lines: List<String>): List<CamelCards> {
    return lines
        .map { it.split(" ") }
        .map { splittedLine -> CamelCards(splittedLine[0], splittedLine[1].toInt()) }
}

private fun part1(input: List<CamelCards>): Int {
    fun cardStrength(char: Char): Int = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').indexOf(char)

    val comparator: Comparator<CamelCards> =
        compareByDescending<CamelCards> { it.cardType(false) }
            .thenBy { cardStrength(it.cards[0]) }
            .thenBy { cardStrength(it.cards[1]) }
            .thenBy { cardStrength(it.cards[2]) }
            .thenBy { cardStrength(it.cards[3]) }
            .thenBy { cardStrength(it.cards[4]) }

    val cardsByRang = input.sortedWith(comparator).reversed()
    return cardsByRang.mapIndexed { index, camelCard -> (index + 1) * camelCard.bid }.sum()
}

private fun part2(input: List<CamelCards>): Int {
    fun cardStrength(char: Char): Int = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').indexOf(char)

    val comparator: Comparator<CamelCards> =
        compareByDescending<CamelCards> { it.cardType(true) }
            .thenBy { cardStrength(it.cards[0]) }
            .thenBy { cardStrength(it.cards[1]) }
            .thenBy { cardStrength(it.cards[2]) }
            .thenBy { cardStrength(it.cards[3]) }
            .thenBy { cardStrength(it.cards[4]) }

    val cardsByRang = input.sortedWith(comparator).reversed()
    return cardsByRang.mapIndexed { index, camelCard -> (index + 1) * camelCard.bid }.sum()
}

private data class CamelCards(val cards: String, val bid: Int) {
    fun cardType(jokerEnabled: Boolean): Int {
        val groupCountBySameLabel = cards.toCharArray().groupBy { it }.mapValues { it.value.size }

        val numberOfJokers = groupCountBySameLabel.getOrDefault('J', 0)

        val jokersHaveToBeConsidered = jokerEnabled && numberOfJokers > 0
        val fiveOfAKind = groupCountBySameLabel.size == 1

        val groupSizes =
            if (jokersHaveToBeConsidered && !fiveOfAKind) {
                val groups = groupCountBySameLabel.filter { it.key != 'J' }.values.sortedDescending().toMutableList()
                groups[0] += numberOfJokers
                groups
            } else {
                groupCountBySameLabel.values.sortedDescending()
            }

        return when {
            groupSizes == listOf(5) -> 7
            groupSizes == listOf(4, 1) -> 6
            groupSizes == listOf(3, 2) -> 5
            groupSizes == listOf(3, 1, 1) -> 4
            groupSizes == listOf(2, 2, 1) -> 3
            groupSizes.contains(2) -> 2
            else -> 1
        }
    }
}
