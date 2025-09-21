@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.util

import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.util.UiText
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
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

fun Instant.isLessThanFiveMinutes(): Boolean {
    val timePassed = Clock.System.now() - this
    return timePassed < 5.minutes
}

fun Instant.toFormattedLastSyncTimeStamp(): UiText {
    if (this == Instant.DISTANT_PAST) {
        return UiText.StringResourceWithArgs(R.string.never_synced)
    }

    val now = Clock.System.now()
    val duration = now - this

    return when {
        duration < 5.minutes -> UiText.StringResourceWithArgs(R.string.just_now)
        duration < 1.days -> {
            val hours = duration.inWholeHours
            val minutes = (duration - hours.hours).inWholeMinutes
            when {
                hours > 0 -> UiText.StringResourceWithArgs(R.string.hours_ago, arrayOf(hours))
                else -> UiText.StringResourceWithArgs(R.string.minutes_ago, arrayOf(minutes))
            }
        }
        duration < 7.days -> {
            val days = duration.inWholeDays
            UiText.StringResourceWithArgs(R.string.days_ago, arrayOf(days))
        }
        else -> {
            UiText.DynamicString(this.toReadableDateTime())
        }
    }
}