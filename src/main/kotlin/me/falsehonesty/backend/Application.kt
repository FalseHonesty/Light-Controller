package me.falsehonesty.backend

import io.micronaut.runtime.Micronaut

object Application

fun main() {
    Micronaut.build()
        .packages("me.falsehonesty.backend")
        .mainClass(Application.javaClass)
        .start()
}
