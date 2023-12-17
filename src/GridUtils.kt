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
        if (n < 0 || n > rows - 1) {
            throw IllegalArgumentException("Invalid parameter $n: should be in 0..<$rows")
        }

        return data[n]
    }

    fun nthCol(n: Int): List<T> {
        if (n < 0 || n > cols - 1) {
            throw IllegalArgumentException("Invalid parameter $n: should be in 0..<$cols")
        }

        return data.map { it[n] }
    }

    fun getOrNull(x: Int, y: Int) = data.getOrNull(y)?.getOrNull(x)
}

data class MutableGrid<T>(
    val data: MutableList<MutableList<T>>,
    val rows: Int = data.size,
    val cols: Int = data[0].size,
) {
    constructor(immutableData: List<List<T>>) : this(immutableData.map { it.toMutableList() }.toMutableList())

    init {
        if (!data.all { it.size == cols }) {
            throw IllegalStateException("invalid grid with different sized rows")
        }
    }

    fun nthRow(n: Int): List<T> {
        if (n < 0 || n > rows - 1) {
            throw IllegalArgumentException("Invalid parameter $n: should be in 0..<$rows")
        }

        return data[n]
    }

    fun nthCol(n: Int): List<T> {
        if (n < 0 || n > cols - 1) {
            throw IllegalArgumentException("Invalid parameter $n: should be in 0..<$cols")
        }

        return data.map { it[n] }
    }

    fun getOrNull(x: Int, y: Int) = data.getOrNull(y)?.getOrNull(x)

    fun toCacheableString(): String {
        return data.joinToString("") { it.joinToString("") }
    }

    fun simpleCopy(): MutableGrid<T> {
        return MutableGrid(data.map { outerList -> outerList.map { it } })
    }
}
