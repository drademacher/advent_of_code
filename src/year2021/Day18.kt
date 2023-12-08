package year2021

import readLines

fun main() {
    fun input() = parseInput(readLines("2021", "day18"))

    fun testInput() = parseInput(readLines("2021", "day18_test"))

    check(part1(testInput()) == 4140)
    println("Part 1:" + part1(input()))

    check(part2(testInput()) == 3993)
    println("Part 2:" + part2(input()))
}

private fun parseInput(lines: List<String>): List<Snailfish> =
    lines
        .map { Snailfish.fromString(it) }

private fun part1(input: List<Snailfish>): Int {
    var acc = input.first()

    for (snailfish in input.drop(1)) {
        acc = Snailfish.add(acc, snailfish)
        acc = acc.reduced()
    }

    return acc.magnitude
}

private fun part2(input: List<Snailfish>): Int {
    return input
        .flatMap { fst -> input.map { snd -> Pair(fst.getCopy(), snd.getCopy()) } }
        .map { Snailfish.add(it.first, it.second) }
        .map(Snailfish::reduced)
        .map(Snailfish::magnitude)
        .maxOf { it }
}

sealed class Snailfish() {
    class Number(var number: Int) : Snailfish()

    class Pair(var left: Snailfish, var right: Snailfish) : Snailfish()

    fun getCopy(): Snailfish {
        return when (this) {
            is Number -> Number(number)
            is Pair -> Pair(left.getCopy(), right.getCopy())
        }
    }

    val isNumberPair: Boolean
        get() = this is Pair && left is Number && right is Number

    val containsNumberPair: Boolean
        get() = this.isNumberPair || (this is Pair && (right.containsNumberPair || left.containsNumberPair))

    fun getInorderTraversal(): List<Number> {
        return when (this) {
            is Number -> listOf(this)
            is Pair -> left.getInorderTraversal() + right.getInorderTraversal()
        }
    }

    fun reduced(): Snailfish {
        var hasExploded = true
        var hasSplit = true

        while (hasExploded || hasSplit) {
            hasExploded = this.explode()
            if (!hasExploded) {
                hasSplit = this.split()
            }
        }

        return this
    }

    fun explode(): Boolean {
        return when (this) {
            is Number -> false
            is Pair -> {
                val current = this.findNumberPairToExplode() ?: return false

                val inorder = getInorderTraversal()
                val offset = inorder.indexOf(current.left)
                val leftSibling = inorder.getOrNull(offset - 1)
                val rightSibling = inorder.getOrNull(offset + 2)

                leftSibling?.also { it.number += (current.left as Number).number }
                rightSibling?.also { it.number += (current.right as Number).number }

                val parent = current.getParent(this)!!
                if (parent.left == current) {
                    parent.left = Number(0)
                } else {
                    parent.right = Number(0)
                }

                return true
            }
        }
    }

    fun findNumberPairToExplode(currentDepth: Int = 0): Pair? {
        if (currentDepth >= 4 && isNumberPair) {
            return this as Pair
        }

        return when (this) {
            is Number -> null
            is Pair -> {
                left.findNumberPairToExplode(currentDepth + 1) ?: right.findNumberPairToExplode(currentDepth + 1)
            }
        }
    }

    fun getParent(tree: Snailfish): Pair? {
        if (tree is Pair && (tree.left == this || tree.right == this)) {
            return tree
        }

        return when (tree) {
            is Number -> null
            is Pair -> {
                getParent(tree.left) ?: getParent(tree.right)
            }
        }
    }

    fun split(parent: Snailfish? = null): Boolean {
        return when (this) {
            is Number -> {
                if (number >= 10) {
                    val split = Pair(Number(number / 2), Number((number + 1) / 2))

                    if ((parent as Pair).left == this) {
                        parent.left = split
                    } else {
                        parent.right = split
                    }

                    true
                } else {
                    false
                }
            }
            is Pair -> {
                left.split(this) || right.split(this)
            }
        }
    }

    val magnitude: Int
        get() =
            when (this) {
                is Number -> number
                is Pair -> 3 * left.magnitude + 2 * right.magnitude
            }

    override fun toString(): String {
        return when (this) {
            is Number -> number.toString()
            is Pair -> "[$left,$right]"
        }
    }

    companion object {
        fun add(
            a: Snailfish,
            b: Snailfish,
        ): Pair {
            return Pair(a, b)
        }

        fun fromString(string: String): Snailfish {
            if (string.toIntOrNull() != null) {
                return Number(string.toInt())
            }

            var level = 0
            var split = -1
            for (index in string.indices) {
                when (string[index]) {
                    '[' -> {
                        level += 1
                    }
                    ']' -> {
                        level -= 1
                    }
                    ',' -> {
                        if (level == 1) {
                            split = index
                            break
                        }
                    }
                }
            }

            if (split == -1) {
                throw IllegalStateException("$string should have a splitting comma")
            }

            return Pair(
                fromString(string.substring(1 until split)),
                fromString(string.substring(split + 1..string.length - 2)),
            )
        }
    }
}
