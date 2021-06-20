package kate.tutorial.kotlin.puzzle

import java.util.UUID

data class PuzzleDetailResponse (
    val id: UUID,
    val avatar: String,
    val author: String,
    val title: String,
    val description: String,
    val tags: String
) {
    companion object {
        operator fun invoke(id: UUID, avatar: String, author: String, title: String, description: String, tags: String? = null) =
            PuzzleDetailResponse(id, avatar, author, title, description, tags ?: "")
    }
}
