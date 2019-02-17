package me.falsehonesty.backend.services.light

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
@Requires(property = "lights.impl", value = "lifx")
class LIFXLightService(@Property(name = "lights.apikey") private val apiKey: String) : LightService {
    private val client = OkHttpClient()
    private val log = LoggerFactory.getLogger(LIFXLightService::class.java)

    private var lastSend = System.currentTimeMillis() - 5000
    private var color: String = "#000000"

    override fun setNewColor(color: String): Boolean {
        if (lastSend + 5000 > System.currentTimeMillis()) {
            return false
        }

        this.color = color
        return sendColorToLIFX(color).also { lastSend = System.currentTimeMillis() }
    }

    override fun getCurrentColor() = color

    override fun getCooldown() = ((System.currentTimeMillis() - lastSend) / 1000).toInt()

    private fun sendColorToLIFX(color: String): Boolean {
        val request = Request.Builder()
            .url("https://api.lifx.com/v1/lights/all/state")
            .put(RequestBody.create(
                MediaType.parse("application/json"),
                getJsonToSend(color).also { log.info("sending $it") }
            ))
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        val call = client.newCall(request).execute()
        val response = call.body()?.string()

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
