@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant


fun Instant.toReadableDate(): String {
    val zonedDateTime = this.toJavaInstant().atZone(ZoneId.systemDefault())
    val currentYear = LocalDate.now().year

    return if (zonedDateTime.year == currentYear) {
        // Format for current year: "19 Apr"
        zonedDateTime.format(DateTimeFormatter.ofPattern("d MMM"))
    } else {
        // Format for previous years: "19 Apr 2024"
        zonedDateTime.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
    }
}


fun Instant.toReadableDateTime(): String {
    val zonedDateTime = this.toJavaInstant().atZone(ZoneId.systemDefault())

    return zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
}