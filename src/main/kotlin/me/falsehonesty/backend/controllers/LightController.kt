package me.falsehonesty.backend.controllers

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.views.ModelAndView
import me.falsehonesty.backend.services.light.LightService
import me.falsehonesty.backend.services.notification.NotificationService
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.Pattern

@Controller("/light")
class LightController(@Inject val lightService: LightService, @Inject val notifService: NotificationService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Get("/")
    fun index(): ModelAndView {
        return ModelAndView("light", LightData(currentColor = lightService.getCurrentColor()))
    }

    @Post("/change-light", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun post(@Valid @Pattern(regexp = "#[0-9a-fA-F]{6}") color: String): ModelAndView {
        val succeeded = lightService.setNewColor(color)

        if (succeeded) {
            notifService.sendNotification("Light Color Change", "Your light's new color is $color")
        }

        return ModelAndView(
            "light",
            if (succeeded) LightData(currentColor = lightService.getCurrentColor(), modifiedColor = true)
            else LightData(
                currentColor = lightService.getCurrentColor(),
                failedModifiedColor = true,
                waitTime = lightService.getCooldown()
            )
        )
    }
}

data class LightData(
    val currentColor: String = "?",
    val modifiedColor: Boolean = false,
    val failedModifiedColor: Boolean = false,
    val waitTime: Int = 0
)
