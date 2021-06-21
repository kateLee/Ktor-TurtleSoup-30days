package kate.tutorial.kotlin.user

import org.jetbrains.exposed.dao.id.UUIDTable

object Users: UUIDTable() {
    val avatar = varchar("avatar", length = 50)
    val name = varchar("name", length = 50)
}
