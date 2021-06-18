package kate.tutorial.kotlin.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*

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
        get("/api/puzzles/{id}") {
            TODO()
        }
        post("/api/puzzles") {
            TODO()
        }
        delete("/api/puzzles/{id}") {
            TODO()
        }
    }

}
