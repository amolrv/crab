package me.amol.crab

import java.io.BufferedInputStream
import java.io.File
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit.DAYS

class CSVCookieFileReader : CookieFileReader {
    override fun readLogsOfTheDay(file: File, date: OffsetDateTime): List<TimestampedCookie> =
        file.inputStream().let { BufferedInputStream(it).reader() }.useLines { lines ->
            lines.dropWhile { it.lowercase() == "cookie,timestamp" }
                .map { line -> Companion.parseCsvLine(line) }
                .dropWhile { it.timestamp.truncatedTo(DAYS) > date }
                .takeWhile { it.timestamp.truncatedTo(DAYS) == date }
                .ifEmpty { emptySequence() }
                .toList()
        }

    companion object {
        private const val cookieColIndex = 0
        private const val timeStampColIndex = 1
        private fun parseCsvLine(line: String) = line.split(",").let { csv ->
            TimestampedCookie(
                cookie = csv.getOrNull(Companion.cookieColIndex) ?: "",
                timestamp = csv.getOrNull(Companion.timeStampColIndex) ?: ""
            )
        }
    }
}
