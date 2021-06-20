package kate.tutorial.kotlin.exceptions

class PuzzleNotFoundException(override val message: String = "Puzzle Not Found"): Exception()
