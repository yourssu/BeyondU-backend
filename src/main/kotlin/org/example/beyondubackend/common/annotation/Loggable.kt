package org.example.beyondubackend.common.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Loggable(
    val level: LogLevel = LogLevel.INFO,
)

enum class LogLevel { INFO, DEBUG }
