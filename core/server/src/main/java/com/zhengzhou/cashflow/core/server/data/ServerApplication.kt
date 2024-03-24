package com.zhengzhou.cashflow.core.server.data

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title

fun Application.module() {

    routing {
        get("/") {
            call.respondHtml(status = HttpStatusCode.OK) {
                head {
                    title {
                        +"Ciao"
                    }
                }
                body {
                    h1 {
                        +"Hello world\n"
                    }
                }
            }
        }
    }
}