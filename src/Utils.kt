import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

fun readLines(
    year: String,
    day: String,
) = Path("src/year$year/resources/$day.txt").readLines()

fun readFile(
    year: String,
    day: String,
) = Path("src/year$year/resources/$day.txt").readText()
