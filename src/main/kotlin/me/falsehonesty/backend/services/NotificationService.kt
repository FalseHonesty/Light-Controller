package me.falsehonesty.backend.services

import io.micronaut.context.annotation.Value
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Singleton

@Singleton
class NotificationService(
    @Value("\${notifications.user}") private val user: String,
    @Value("\${notifications.token}") private val token: String
) {
    private val client = OkHttpClient()

    fun sendNotification(title: String, message: String) {
        val request = Request.Builder()
            .url("https://api.pushover.net/1/messages.json")
            .post(
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", token)
                    .addFormDataPart("user", user)
                    .addFormDataPart("title", title)
                    .addFormDataPart("message", message)
                    .build()
            )
            .build()

        client.newCall(request).execute()
    }
}
