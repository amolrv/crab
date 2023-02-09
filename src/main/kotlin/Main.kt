import me.amol.crab.ActiveCookieCalculator
import me.amol.crab.ActiveCookieCommand
import me.amol.crab.CSVCookieFileReader

fun main(args: Array<String>) = ActiveCookieCommand(CSVCookieFileReader(), ActiveCookieCalculator()).main(args)
