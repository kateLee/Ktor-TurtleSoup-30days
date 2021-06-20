package kate.tutorial.kotlin.puzzle

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime

object Puzzles : UUIDTable() {
    val title = varchar("title", length = 50)
    val description = text("description")
    val tags = text("tags")
    val author = varchar("author", length = 50)
    val avatar = varchar("avatar", length = 50)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
}
