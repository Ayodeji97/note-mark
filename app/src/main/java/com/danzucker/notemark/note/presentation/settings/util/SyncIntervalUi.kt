package com.danzucker.notemark.note.presentation.settings.util

import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.util.UiText

enum class SyncIntervalUi(
    val title: UiText,
    val timeInterval: Int,
    val timeIntervalUnit: SyncTimeUnit
) {
    MANUAL(
        title = UiText.StringResourceWithArgs(R.string.manual_only),
        timeInterval = 0,
        timeIntervalUnit = SyncTimeUnit.NONE
    ),

    FIFTEEN_MINUTES(
        title = UiText.StringResourceWithArgs(R.string.sync_interval_15_minutes),
        timeInterval = 15,
        timeIntervalUnit = SyncTimeUnit.MINUTES
    ),

    THIRTY_MINUTES(
        title = UiText.StringResourceWithArgs(R.string.sync_interval_30_minutes),
        timeInterval = 30,
        timeIntervalUnit = SyncTimeUnit.MINUTES
    ),

    ONE_HOUR(
        title = UiText.StringResourceWithArgs(R.string.sync_interval_1_hour),
        timeInterval = 1,
        timeIntervalUnit = SyncTimeUnit.HOURS
    )
}

enum class SyncTimeUnit {
    NONE,
    MINUTES,
    HOURS,
    DAYS
}

