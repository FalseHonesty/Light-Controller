package me.falsehonesty.backend.services.notification

import io.micronaut.retry.annotation.Fallback
import javax.inject.Singleton

@Singleton
@Fallback
class NoOpNotificationService : NotificationService {
    override fun sendNotification(title: String, message: String) = Unit
}
