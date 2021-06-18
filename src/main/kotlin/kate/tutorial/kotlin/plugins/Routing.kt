package kate.tutorial.kotlin.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import kate.tutorial.kotlin.puzzle.PuzzleResponse
import java.util.*
import kotlin.collections.ArrayList

fun Application.configureRouting() {
    // Starting point for a Ktor app:
    install(ContentNegotiation) {
        gson {
        }
    }
    routing {
        get("/") {
            call.respond(mapOf("message" to "HELLO WORLD!"))
        }
        get("/api/puzzles") {
            val list = ArrayList<PuzzleResponse>()
            for (i in 0..10) {
                list.add(
                    PuzzleResponse(
                        id = UUID.randomUUID(),
                        title = "從前從前有碗" + listOf("海龜湯", "孟婆湯", "玉米湯", "南瓜湯").random(),
                        avatar = "https://imgur.com/l0swFL1.jpg",
                        attendance = (0..10).random().toString() + "人")
                )
            }
            call.respond(list)
        }
        post("/api/puzzles") {
            TODO()
        }
        get("/api/puzzles/{id}") {
            TODO()
        }
        delete("/api/puzzles/{id}") {
            TODO()
        }
    }

}
