import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readInput(
    year: String,
    day: String,
) = Path("src/year$year/resources/$day.txt").readLines()
