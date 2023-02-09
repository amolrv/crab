package me.amol.crab

class ValidationError(val value: String, override val cause: Throwable? = null) : Throwable(value, cause)
