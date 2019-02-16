package me.falsehonesty.backend.services

import io.micronaut.context.annotation.Value
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LightService(
    @Inject private val notificationService: NotificationService,
    @Value("\${lights.apikey}") private val apiKey: String
) {
    private val client = OkHttpClient()
    private val log = LoggerFactory.getLogger(LightService::class.java)

    var lastSend = System.currentTimeMillis()
    var color: String = "#000000"

    fun setNewColor(value: String): Boolean {
        if (lastSend + 5000 > System.currentTimeMillis()) {
            return false
        }

        color = value
        return sendColorToLIFX(value).also { success ->
            lastSend = System.currentTimeMillis()

            if (success)
                notificationService.sendNotification("Light Color Change", "Your light's new color is $value")
        }
    }

    private fun sendColorToLIFX(color: String): Boolean {
        val request = Request.Builder()
            .url("https://api.lifx.com/v1/lights/all/state")
            .put(RequestBody.create(
                MediaType.parse("application/json"),
                getJsonToSend(color).also { log.info("sending $it") }
            ))
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        val response = client.newCall(request).execute().body()?.string()

        return response?.contains("error") != true
    }

    private fun getJsonToSend(color: String): String {
        return """
            {
                "power": "on",
                "color": "$color",
                "duration": 5,
                "fast": true
            }
        """.trimIndent()
    }
}
