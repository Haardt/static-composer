package de.conpinion.sc.controller

import de.conpinion.sc.Upload
import de.conpinion.sc.composer.ComposerService
import org.jetbrains.ktor.locations.post
import org.jetbrains.ktor.request.PartData
import org.jetbrains.ktor.request.contentType
import org.jetbrains.ktor.request.isMultipart
import org.jetbrains.ktor.request.receiveMultipart
import org.jetbrains.ktor.response.respondWrite
import org.jetbrains.ktor.routing.Route
import java.io.File

fun Route.upload(composerService: ComposerService) {
    post<Upload> { desc ->

        call.respondWrite {
            if (call.request.isMultipart()) {
                val multipart = call.receiveMultipart()
                multipart.parts.forEach { part ->
                    appendln(when (part) {
                        is PartData.FileItem -> {
                            val filename = part.originalFileName
                            val file = File.createTempFile("composer", ".zip")
                            part.streamProvider().use { its -> file.outputStream().buffered().use { its.copyTo(it) } }
                            composerService.updateStaticPart(desc, filename, file.absolutePath)
                            "OK"
                        }
                        else -> "No file in request found..."
                    })
                    part.dispose
                }
            } else {
                appendln("Not a multipart request")
            }
        }
    }
}
