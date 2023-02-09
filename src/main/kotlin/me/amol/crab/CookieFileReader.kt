package me.amol.crab

import java.io.File
import java.time.OffsetDateTime

fun interface CookieFileReader {
    fun readLogsOfTheDay(file: File, date: OffsetDateTime): List<TimestampedCookie>
}
