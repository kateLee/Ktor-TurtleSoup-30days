package kate.tutorial.kotlin.puzzle

import kate.tutorial.kotlin.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime

object Puzzles : UUIDTable() {
    val title = varchar("title", length = 50)
    val description = text("description")
    val tags = text("tags")
    val author = reference("author", Users)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
}
