@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.notedetails

import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.util.UiText
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.presentation.notedetails.screenMode.ScreenMode
import com.danzucker.notemark.note.presentation.util.toReadableDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class NoteDetailsState(
    val id: String = "",
    val titleText: String = "",
    val contentText: String = "",
    val originalText: String = "",
    val originalContext: String = "",
    val createdAt: Instant = Clock.System.now(),
    val lastEditAt: Instant = Clock.System.now(),
    val showDiscardConfirmationDialog: Boolean = false,
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val saveStatus: NoteSaveStatus = NoteSaveStatus.DRAFT,
    val screenMode: ScreenMode = ScreenMode.View, // Default to View mode
    val isReaderUiVisible: Boolean = false,

) {
    val formattedCreatedAt: String
        get() = createdAt.toReadableDateTime()

    val formattedLastEditAt: String
        get() = if (lastEditAt == Clock.System.now()) {
            "Just now" // This can be localized if needed
        } else {
            lastEditAt.toReadableDateTime()
        }

    val isViewMode: Boolean
        get() = screenMode == ScreenMode.View

    val isEditMode: Boolean
        get() = screenMode == ScreenMode.Edit

    val isReaderMode: Boolean
        get() = screenMode == ScreenMode.Reader

    val hasUnSavedChanges: Boolean
        get() = titleText != originalText || contentText != originalContext
}