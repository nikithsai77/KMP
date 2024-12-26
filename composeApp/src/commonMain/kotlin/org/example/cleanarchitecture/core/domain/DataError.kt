package org.example.cleanarchitecture.core.domain

import org.example.cleanarchitecture.core.presentation.UIText

sealed interface DataError {
    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }
    enum class Local: DataError {
        DISK_FULL,
        UNKNOWN
    }
}

fun DataError.toUiText(): UIText {
    val stringRes = when(this) {
        DataError.Local.DISK_FULL -> "disk full"
        DataError.Local.UNKNOWN -> "error unknown"
        DataError.Remote.SERVER -> "Server Error"
        DataError.Remote.UNKNOWN -> "error unknown"
        DataError.Remote.NO_INTERNET -> "No Internet"
        DataError.Remote.SERIALIZATION -> "error serialization"
        DataError.Remote.REQUEST_TIMEOUT -> "error request timeout"
        DataError.Remote.TOO_MANY_REQUESTS -> "error too many requests"
    }
    return UIText.DynamicString(stringRes)
}