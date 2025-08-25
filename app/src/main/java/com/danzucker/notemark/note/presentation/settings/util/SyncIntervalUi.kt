package com.danzucker.notemark.note.presentation.settings.util

import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.util.UiText
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class SyncIntervalUi(
    val title: UiText,
    val timeInterval: Duration
) {
    MANUAL(
        title = UiText.StringResourceWithArgs(R.string.manual_only),
        timeInterval = Duration.ZERO
    ),

    FIFTEEN_MINUTES(
        title = UiText.StringResourceWithArgs(R.string.sync_interval_15_minutes),
        timeInterval = 15.minutes
    ),

    THIRTY_MINUTES(
        title = UiText.StringResourceWithArgs(R.string.sync_interval_30_minutes),
        timeInterval = 30.minutes
    ),

    ONE_HOUR(
        title = UiText.StringResourceWithArgs(R.string.sync_interval_1_hour),
        timeInterval = 60.minutes
    )
}

