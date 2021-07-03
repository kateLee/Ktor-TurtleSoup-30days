package kate.tutorial.kotlin.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kate.tutorial.kotlin.exceptions.BadParamException
import kate.tutorial.kotlin.exceptions.IllegalPuzzleIdException
import kate.tutorial.kotlin.exceptions.PuzzleNotFoundException
import kate.tutorial.kotlin.puzzle.*
import kate.tutorial.kotlin.user.User
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Duration
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

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(60) // Disabled (null) by default
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE // Disabled (max value). The connection will be closed if surpassed this length.
        masking = false
    }

    Database.connect("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(Puzzles)
    }
    transaction {
        val user = User.new {
            name = "Kate"
            avatar = "https://imgur.com/l0swFL1.jpg"
        }
        for (i in 0..10) {
            Puzzle.new {
                author = user
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
                Puzzle.all().with(Puzzle::author).map {
                    PuzzleResponse(
                        id = it.id.value,
                        title = it.title,
                        avatar = it.author.avatar,
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
                    author = User.all().first()
                    title = request.title ?: throw BadParamException()
                    description = request.description ?: throw BadParamException()
                    tags= request.tags ?: throw BadParamException()
                }.run {
                    PuzzleDetailResponse(
                        id = id.value,
                        title = title,
                        avatar = author.avatar,
                        author = author.name,
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
                        avatar = author.avatar,
                        author = author.name,
                        description = description,
                        tags = tags
                    )
                } ?: throw PuzzleNotFoundException()
            }
            call.respond(puzzle)
        }
        delete("/api/puzzles/{id}") {
            val puzzleId = try { UUID.fromString(call.parameters["id"]) } catch (e: Exception) { throw IllegalPuzzleIdException() }
            transaction {
                Puzzle.findById(puzzleId)?.delete() ?: throw PuzzleNotFoundException()
            }
            call.respond(HttpStatusCode.NoContent)
        }

        webSocket("/chat") {
            for (frame in incoming) {
                if (frame is Frame.Text)  {
                    val text = frame.readText()
                    outgoing.send(Frame.Text("[user]: $text"))
                    if (text.equals("bye", ignoreCase = true)) {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }
            }
        }
    }
}
