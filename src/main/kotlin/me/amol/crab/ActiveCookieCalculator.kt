package me.amol.crab

class ActiveCookieCalculator {
    fun activeCookies(timestampedCookies: List<TimestampedCookie>): List<String> =
        timestampedCookies.groupBy({ it.cookie }, { 1 })
            .mapValues { (_, values) -> values.size }
            .asSequence()
            .map { (cookie, count) -> count to cookie }
            .groupBy({ (count, _) -> count }, { (_, cookie) -> cookie })
            .toSortedMap(compareByDescending { it })
            .let { countToCookies ->
                countToCookies.takeIf { it.isNotEmpty() }
                    ?.let { countToCookies[countToCookies.firstKey()] }
                    ?: emptyList()
            }
}
