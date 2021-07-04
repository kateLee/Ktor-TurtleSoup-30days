package kate.tutorial.kotlin

import io.ktor.application.*
import kate.tutorial.kotlin.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureRouting()
}
