package me.falsehonesty.backend.services.light

import io.micronaut.retry.annotation.Fallback
import javax.inject.Singleton

@Singleton
@Fallback
class NoOpLightService : LightService {
    override fun setNewColor(color: String) = true

    override fun getCurrentColor() = "#000000"

    override fun getCooldown() = 0
}
