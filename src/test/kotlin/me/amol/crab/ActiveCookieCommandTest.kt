package me.amol.crab

import com.github.ajalt.clikt.core.BadParameterValue
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ActiveCookieCommandTest {

    @Test
    fun `should parse args date correctly`() {
        val mockCookieReader = CookieFileReader { _, _ -> emptyList() }
        val activeCookieCommand = ActiveCookieCommand(mockCookieReader, ActiveCookieCalculator())
        shouldNotThrowAny { activeCookieCommand.parse(arrayOf("-f", "cookie-sample.csv", "-d", "2018-12-09")) }
    }

    @Test
    fun `should throw bad parameter error`() {
        val mockCookieReader = CookieFileReader { _, _ -> emptyList() }
        val activeCookieCommand = ActiveCookieCommand(mockCookieReader, ActiveCookieCalculator())
        shouldThrowExactly<BadParameterValue> {
            activeCookieCommand.parse(
                arrayOf(
                    "-f",
                    "cookie-sample.csv",
                    "-d",
                    "12-09-2018"
                )
            )
        }
            .should {
                it.message shouldBe "Invalid value for \"-d\": Text '12-09-2018' could not be parsed at index 0"
            }
    }

    @Test
    fun `should print active records for given date`() {
        val activeCookie = ActiveCookieCalculatorTest.makeRandomCookie()
        val mockCookieReader = CookieFileReader { _, _ ->
            listOf(
                activeCookie,
                ActiveCookieCalculatorTest.makeRandomCookie(),
                activeCookie.copy(timestamp = activeCookie.timestamp.plusDays(10)),
                ActiveCookieCalculatorTest.makeRandomCookie()
            )
        }

        val byteArrayOutputStream = ByteArrayOutputStream().also { System.setOut(PrintStream(it)) }
        val activeCookieCommand = ActiveCookieCommand(mockCookieReader, ActiveCookieCalculator())
        activeCookieCommand.parse(arrayOf("-f", "cookie-sample.csv", "-d", "2018-12-09"))

        byteArrayOutputStream.toString() shouldBe "${activeCookie.cookie}\n"
    }
}
