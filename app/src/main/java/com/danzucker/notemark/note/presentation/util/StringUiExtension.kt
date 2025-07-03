package com.danzucker.notemark.note.presentation.util

import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType

fun String.truncateForPreview(deviceScreenType: DeviceScreenType): String {
    val maxChars = when (deviceScreenType) {
        DeviceScreenType.MOBILE_PORTRAIT,
        DeviceScreenType.MOBILE_LANDSCAPE -> 150
        DeviceScreenType.TABLET_PORTRAIT,
        DeviceScreenType.TABLET_LANDSCAPE,
        DeviceScreenType.DESKTOP -> 250
    }

    return previewWithEllipsis(maxChars)
}

fun String.previewWithEllipsis(maxChars: Int): String {
    return if (this.length > maxChars) {
        this.take(maxChars).trimEnd() + "…"
    } else {
        this
    }
}