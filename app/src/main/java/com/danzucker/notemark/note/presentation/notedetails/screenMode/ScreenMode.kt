package com.danzucker.notemark.note.presentation.notedetails.screenMode

sealed interface ScreenMode {
    data object View : ScreenMode

    data object Edit : ScreenMode

    data object Reader : ScreenMode
}