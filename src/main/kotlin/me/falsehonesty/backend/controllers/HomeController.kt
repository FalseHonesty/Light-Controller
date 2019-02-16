package me.falsehonesty.backend.controllers

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.net.URI

@Controller("/")
class HomeController {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Get("/")
    fun index() = HttpResponse.temporaryRedirect<Any>(URI.create("/light"))
}
