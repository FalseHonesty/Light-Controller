package me.falsehonesty.backend.services.notification

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Singleton

@Singleton
@Requires(property = "notifications.impl", value = "pushover")
class PushoverNotificationService(
    @Property(name = "notifications.user") private val user: String,
    @Property(name = "notifications.token") private val token: String
) : NotificationService {
    private val client = OkHttpClient()

    override fun sendNotification(title: String, message: String) {
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
