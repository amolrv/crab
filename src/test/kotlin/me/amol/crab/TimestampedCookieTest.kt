package me.amol.crab

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.time.ZoneOffset.ofHours

class TimestampedCookieTest {
    @Test
    fun `should create cookie timestamp from input`() {
        val cookie = "AtY0laUfhglK3lC7"
        val timestamp = "2018-12-09T14:19:00+00:00"
        val timestampedCookie = TimestampedCookie(cookie, timestamp)
        val validTimestampedCookie = TimestampedCookie(
            cookie,
            OffsetDateTime.of(2018, 12, 9, 14, 19, 0, 0, UTC)
        )

        timestampedCookie shouldBe validTimestampedCookie
    }

    @Test
    fun `should parse offset information correctly`() {
        val cookie = "AtY0laUfhglK3lC7"
        val timestamp = "2018-12-09T14:19:00+01:00"
        val timestampedCookie = TimestampedCookie(cookie, timestamp)
        val validTimestampedCookie = TimestampedCookie(
            cookie,
            OffsetDateTime.of(2018, 12, 9, 14, 19, 0, 0, ofHours(1))
        )

        timestampedCookie shouldBe validTimestampedCookie
    }

    @Test
    fun `should be invalid when cookies is empty or whitespace`() {
        shouldThrowExactly<ValidationError> { TimestampedCookie("  ", "2018-12-09T14:19:00+00:00") }
            .shouldBe(ValidationError(value = "Cookie must not be blank, found '  '"))
    }

    @Test
    fun `should be invalid when timestamp is invalid`() {
        shouldThrowExactly<ValidationError> {
            TimestampedCookie("AtY0laUfhglK3lC7", "2018-12-09T14:19:00")
        }.shouldBe(ValidationError(value = "Cookie timestamp must be in '2011-12-03T10:15:30+01:00' format. Found 2018-12-09T14:19:00"))
    }
}
