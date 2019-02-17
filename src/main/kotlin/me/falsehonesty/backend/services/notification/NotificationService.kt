package me.falsehonesty.backend.services.notification

interface NotificationService {
    fun sendNotification(title: String, message: String)
}
