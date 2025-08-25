package com.danzucker.notemark.note.domain.note.mappers

import com.danzucker.notemark.note.domain.note.sync.SyncInterval
import com.danzucker.notemark.note.presentation.settings.util.SyncIntervalUi

fun SyncIntervalUi.toSyncInterval(): SyncInterval {
    return when (this) {
        SyncIntervalUi.MANUAL -> SyncInterval.MANUAL
        SyncIntervalUi.FIFTEEN_MINUTES -> SyncInterval.FIFTEEN_MINUTES
        SyncIntervalUi.THIRTY_MINUTES -> SyncInterval.THIRTY_MINUTES
        SyncIntervalUi.ONE_HOUR -> SyncInterval.ONE_HOUR
    }
}

fun SyncInterval.toSyncIntervalUi(): SyncIntervalUi {
    return when (this) {
        SyncInterval.MANUAL -> SyncIntervalUi.MANUAL
        SyncInterval.FIFTEEN_MINUTES -> SyncIntervalUi.FIFTEEN_MINUTES
        SyncInterval.THIRTY_MINUTES -> SyncIntervalUi.THIRTY_MINUTES
        SyncInterval.ONE_HOUR -> SyncIntervalUi.ONE_HOUR
    }
}