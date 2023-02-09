package me.amol.crab

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

data class TimestampedCookie(val cookie: String, val timestamp: OffsetDateTime) {
    companion object {
        private fun parseCookieTimeStamp(timestamp: String): OffsetDateTime =
            runCatching { OffsetDateTime.parse(timestamp, ISO_OFFSET_DATE_TIME) }.getOrElse {
                throw ValidationError(
                    "Cookie timestamp must be in '2011-12-03T10:15:30+01:00' format. Found $timestamp",
                    it
                )
            }

        private fun validateCookie(value: String) = value.ifBlank {
            throw ValidationError("Cookie must not be blank, found '$value'")
        }

        operator fun invoke(cookie: String?, timestamp: String?): TimestampedCookie =
            TimestampedCookie(
                validateCookie(cookie ?: ""),
                parseCookieTimeStamp(timestamp ?: "")
            )
    }
}
