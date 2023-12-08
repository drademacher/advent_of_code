data class Point(val x: Int, val y: Int) {
    fun add(other: Point): Point = Point(this.x + other.x, this.y + other.y)
}
