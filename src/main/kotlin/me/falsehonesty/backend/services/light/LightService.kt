package me.falsehonesty.backend.services.light

interface LightService {
    /**
     * Sets a new color for the lights.
     *
     * @param color a hex, RGB formatted string describing the new color
     * @return whether or not the operation was successful
     */
    fun setNewColor(color: String): Boolean

    /**
     * Get the current color of the lights.
     *
     * @return a hex, RGB formatted string describing the current color
     */
    fun getCurrentColor(): String

    /**
     * Gets the cooldown before the lights can be changed again.
     *
     * @return the cooldown in seconds
     */
    fun getCooldown(): Int
}
