package me.amol.crab

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import java.io.File
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

class ActiveCookieCommand(
    private val csvCookieFileReader: CookieFileReader,
    private val activeCookieCalculator: ActiveCookieCalculator
) :
    CliktCommand(name = "cookie") {
    private val file: File by option("-f", help = "cookie file path")
        .file(mustExist = true, canBeDir = false, mustBeReadable = true)
        .required()

    private val date: OffsetDateTime by option("-d", help = "date in yyyy-MM-dd format")
        .convert { offsetDateTime(it) }.required()

    companion object {
        private fun offsetDateTime(it: String): OffsetDateTime =
            OffsetDateTime.of(LocalDate.parse(it, ISO_LOCAL_DATE).atStartOfDay(), UTC)
    }

    override fun run() {
        with(csvCookieFileReader) {
            readLogsOfTheDay(file, date)
                .let { activeCookieCalculator.activeCookies(it) }
                .forEach { println(it) }
        }
    }
}
