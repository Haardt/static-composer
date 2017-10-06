package de.conpinion.sc

import de.conpinion.sc.composer.ComposerService
import de.conpinion.sc.controller.upload
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.routing.Routing
import org.jetbrains.ktor.gson.*
import org.jetbrains.ktor.locations.*

data class Item(val key: String, val value: String)


@location("/") class index()
@location("/json") class json()
@location("/upload/{project}/{branch}") data class Upload(val project: String, val branch: String)

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)
    install(GsonSupport) {
        setPrettyPrinting()
    }
    val composerService = ComposerService()
    install(Routing) {
        upload(composerService)
    }
    /*
    routing {
        get<index> {
            val contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)

            call.respondHtml {
                head {
                    title { +"POST" }
                    meta {
                        httpEquiv = HttpHeaders.ContentType
                        content = contentType.toString()
                    }
                }
                body {
                    p {
                        +"File upload example"
                    }
//                    form(locations.href(post()), encType = FormEncType.multipartFormData, method = FormMethod.post) {
//                        acceptCharset = "utf-8"
//                        textInput { name = "field1" }
//                        fileInput { name = "file1" }
//                        submitInput { value = "send" }
//                    }
                }
            }

        }

        get<json> {
            val testItem = Item("test", "test2")

            call.respond(testItem)
        }
    }
    */
}
