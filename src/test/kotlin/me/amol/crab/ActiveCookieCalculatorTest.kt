package me.amol.crab

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.random.Random

class ActiveCookieCalculatorTest {
    private val activeCookieCalculator = ActiveCookieCalculator()

    @Test
    fun `should return empty list when input list is empty`() {
        activeCookieCalculator.activeCookies(emptyList()).shouldBeEmpty()
    }

    @Test
    fun `should return active cookies which has max number occurrences regardless of timestamp`() {
        val activeCookie = makeRandomCookie()
        val cookies = listOf(
            activeCookie,
            makeRandomCookie(),
            activeCookie.copy(timestamp = activeCookie.timestamp.plusDays(10)),
            makeRandomCookie()
        )

        activeCookieCalculator.activeCookies(cookies) shouldBe listOf(activeCookie.cookie)
    }

    @Test
    fun `should return all cookies when every cookies has same number of occurrence`() {
        val activeCookies = listOf(makeRandomCookie(), makeRandomCookie(), makeRandomCookie())

        val mixedCookies =
            activeCookies + listOf(makeRandomCookie(), makeRandomCookie(), makeRandomCookie()) + activeCookies

        activeCookieCalculator.activeCookies(mixedCookies) shouldBe activeCookies.map { it.cookie }
    }

    companion object {
        fun makeRandomCookie(
            value: String = randomStrings(10),
            localDate: LocalDate = LocalDate.now()
        ): TimestampedCookie =
            TimestampedCookie(value, OffsetDateTime.of(localDate.atStartOfDay(), ZoneOffset.UTC))

        private fun randomStrings(size: Int = 10) = (1..size)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")

        private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    }
}
