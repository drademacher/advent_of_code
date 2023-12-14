import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.sqrt

fun readLines(
    year: String,
    day: String,
) = Path("src/year$year/resources/$day.txt").readLines()

fun readFile(
    year: String,
    day: String,
) = Path("src/year$year/resources/$day.txt").readText()

fun getPrimeTester(n: Int): (Int) -> Boolean {
    val sieve = BooleanArray(n / 3 + 1) { it != 0 }

    fun inWheel(k: Int) = k % 6 == 1 || k % 6 == 5

    fun toWheel(k: Int) = k / 3

    (1..sqrt(n.toDouble()).toInt() step 2)
        .filter { inWheel(it) && sieve[toWheel(it)] }
        .forEach { i -> (i * i..n step i).filter { inWheel(it) }.forEach { j -> sieve[toWheel(j)] = false } }

    return { it -> it == 2 || it == 3 || (it in 1..n && inWheel(it) && sieve[toWheel(it)]) }
}

fun getPrimesBelow(n: Int): List<Int> {
    val primeTester = getPrimeTester(n)
    return (1..n).filter(primeTester)
}

fun powerOfTwo(n: Int) = 1 shl n

fun nthRightBinaryDigitIsOne(
    x: Int,
    n: Int,
) = x and (1 shl n) != 0

data class Grid<T>(
    val data: List<List<T>>,
    val rows: Int = data.size,
    val cols: Int = data[0].size,
) {
    init {
        if (!data.all { it.size == cols }) {
            throw IllegalStateException("invalid grid with different sized rows")
        }
    }

    fun nthRow(n: Int): List<T> {
        if (n < 1 || n > rows) {
            throw IllegalArgumentException("Invalid parameter $n: should be in 1..$rows")
        }

        return data[n - 1]
    }

    fun nthCol(n: Int): List<T> {
        if (n < 1 || n > cols) {
            throw IllegalArgumentException("Invalid parameter $n: should be in 1..$cols")
        }

        return data.map { it[n - 1] }
    }
}
