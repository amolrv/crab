package me.amol.crab

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC

class CSVCookieFileReaderTest {

    private val csvCookieFileReader = CSVCookieFileReader()

    @Test
    fun `should return all the logs only for given day`() {
        val expected = listOf(
            TimestampedCookie(cookie = "SAZuXPGUrfbcn5UA", timestamp = "2018-12-08T22:03Z"),
            TimestampedCookie(cookie = "4sMM2LxV07bPJzwf", timestamp = "2018-12-08T21:30Z"),
            TimestampedCookie(cookie = "fbcn5UAVanZf6UtG", timestamp = "2018-12-08T09:30Z")
        )
        csvCookieFileReader.readLogsOfTheDay(validFile, utcDate(2018, 12, 8))
            .shouldBe(expected)
    }

    @Test
    fun `should return cookies based on date`() {
        val expected = listOf(TimestampedCookie("4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00"))

        csvCookieFileReader.readLogsOfTheDay(validFile, utcDate(2018, 12, 7))
            .shouldBe(expected)
    }

    @Test
    fun `should return empty list when there is no matching log entries found`() {
        csvCookieFileReader.readLogsOfTheDay(validFile, utcDate(2022, 12, 7))
            .shouldBeEmpty()
    }

    @Test
    fun `should return matched entries when csv file is without headers`() {
        val expected = listOf(TimestampedCookie("4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00"))
        csvCookieFileReader.readLogsOfTheDay(validFileWithoutHeaders, utcDate(2018, 12, 7))
            .shouldBe(expected)
    }

    @Test
    fun `should return empty list when log file is empty`() {

        csvCookieFileReader.readLogsOfTheDay(emptyFile, utcDate(2018, 12, 7))
            .shouldBeEmpty()

        csvCookieFileReader.readLogsOfTheDay(justHeaders, utcDate(2018, 12, 7))
            .shouldBeEmpty()
    }

    @Test
    fun `should throw validation error when failed to parse a file`() {
        shouldThrowExactly<ValidationError> {
            csvCookieFileReader.readLogsOfTheDay(invalidFile, utcDate(2018, 12, 7))
        }.message.shouldBe("Cookie timestamp must be in '2011-12-03T10:15:30+01:00' format. Found cookie")
    }

    companion object {

        val validFile = fileByName("valid-file.csv")
        val validFileWithoutHeaders = fileByName("valid-file-without-headers.csv")
        val justHeaders = fileByName("just-headers.csv")
        val emptyFile = fileByName("empty-file.csv")
        val invalidFile = fileByName("valid-file-invalid-entries.csv")
        fun utcDate(year: Int, month: Int, dayOfMonth: Int): OffsetDateTime =
            LocalDate.of(year, month, dayOfMonth)
                .atStartOfDay()
                .let { OffsetDateTime.of(it, UTC) }

        private fun fileByName(fileName: String) =
            this::class.java.classLoader.getResource(fileName).toURI().let(::File)
    }
}
