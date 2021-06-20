package kate.tutorial.kotlin.puzzle

import java.util.*

data class PuzzleResponse (
    val id: UUID,
    val avatar: String,
    val title: String,
    val attendance: String,
    val tags: String,
)
