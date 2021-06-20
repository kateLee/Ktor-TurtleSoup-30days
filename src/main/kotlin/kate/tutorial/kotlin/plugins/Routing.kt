package kate.tutorial.kotlin.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kate.tutorial.kotlin.exceptions.BadParamException
import kate.tutorial.kotlin.exceptions.IllegalPuzzleIdException
import kate.tutorial.kotlin.exceptions.PuzzleNotFoundException
import kate.tutorial.kotlin.puzzle.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Application.configureRouting() {
    // Starting point for a Ktor app:
    install(ContentNegotiation) {
        gson {
        }
    }
    install(StatusPages) {
        exception<BadParamException> { cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("message" to cause.message))
        }
        exception<PuzzleNotFoundException> { cause ->
            call.respond(HttpStatusCode.NotFound, mapOf("message" to cause.message))
        }
    }
    Database.connect("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(Puzzles)
    }
    transaction {
        for (i in 0..10) {
            Puzzle.new {
                author = "Kate"
                avatar = listOf("https://imgur.com/l0swFL1.jpg", "https://imgur.com/ICOyx7j.jpg").random()
                title = "從前從前有碗" + listOf("海龜湯", "孟婆湯", "玉米湯", "南瓜湯").random()
                description = "世界......\n需要更多力量......"
                tags = listOf("原創", "動漫小說戲劇衍生", "驚悚", "生活").random()
            }
        }
    }
    routing {
        get("/") {
            call.respond(mapOf("message" to "HELLO WORLD!"))
        }
        get("/api/puzzles") {
            val response = transaction {
                Puzzle.all().map {
                    PuzzleResponse(
                        id = it.id.value,
                        title = it.title,
                        avatar = it.avatar,
                        attendance = (0..10).random().toString() + "人",
                        tags = it.tags
                    )
                }
            }
            call.respond(response)
        }
        post("/api/puzzles") {
            val request = call.receive<PuzzleRequest>()
            val puzzle = transaction {
                Puzzle.new {
                    author = "Kate"
                    avatar = "https://imgur.com/l0swFL1.jpg"
                    title = request.title ?: throw BadParamException()
                    description = request.description ?: throw BadParamException()
                    tags= request.tags ?: throw BadParamException()
                }.run {
                    PuzzleDetailResponse(
                        id = id.value,
                        title = title,
                        avatar = avatar,
                        author = author,
                        description = description,
                        tags = tags
                    )
                }
            }
            call.respond(puzzle)
        }
        get("/api/puzzles/{id}") {
            val puzzleId = try { UUID.fromString(call.parameters["id"]) } catch (e: Exception) { throw IllegalPuzzleIdException() }
            val puzzle = transaction {
                Puzzle.findById(puzzleId)?.run {
                    PuzzleDetailResponse(
                        id = puzzleId,
                        title = title,
                        avatar = avatar,
                        author = author,
                        description = description,
                        tags = tags
                    )
                } ?: throw PuzzleNotFoundException()
            }
            call.respond(puzzle)
        }
        delete("/api/puzzles/{id}") {
            TODO()
        }
    }

}
