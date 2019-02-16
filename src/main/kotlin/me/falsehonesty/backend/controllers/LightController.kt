package me.falsehonesty.backend.controllers

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.views.ModelAndView
import me.falsehonesty.backend.services.LightService
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.Pattern

@Controller("/light")
class LightController(@Inject val service: LightService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Get("/")
    fun index(): ModelAndView {
        return ModelAndView("light", LightData(currentColor = service.color))
    }

    @Post("/change-light", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun post(@Valid @Pattern(regexp = "#[0-9a-fA-F]{6}") color: String): ModelAndView {
        val succeeded = service.setNewColor(color)

        return ModelAndView(
            "light",
            if (succeeded) LightData(currentColor = service.color, modifiedColor = true)
            else LightData(
                currentColor = service.color,
                failedModifiedColor = true,
                waitTime = (System.currentTimeMillis() - service.lastSend) / 1000
            )
        )
    }
}

data class LightData(
    val currentColor: String = "?",
    val modifiedColor: Boolean = false,
    val failedModifiedColor: Boolean = false,
    val waitTime: Long = 0
)
