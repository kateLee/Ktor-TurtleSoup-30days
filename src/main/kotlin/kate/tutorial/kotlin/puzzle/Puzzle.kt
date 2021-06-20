package kate.tutorial.kotlin.puzzle

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class Puzzle(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Puzzle>(Puzzles)

    var author by Puzzles.author
    var avatar by Puzzles.avatar
    var title by Puzzles.title
    var description by Puzzles.description
    var tags by Puzzles.tags
    var createdAt by Puzzles.createdAt
}
