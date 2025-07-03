@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMedium16
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMediumLarge20
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.*
import com.danzucker.notemark.note.models.NoteUi
import com.danzucker.notemark.note.presentation.preview.NotePreviewModel.noteUi
import kotlin.time.ExperimentalTime

@Composable
fun NoteList(
    notes: List<NoteUi>,
    deviceScreenType: DeviceScreenType,
    onNoteLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (columnCount, contentPadding) = when (deviceScreenType) {
        MOBILE_PORTRAIT -> 2 to paddingMedium16
        MOBILE_LANDSCAPE -> 3 to paddingMedium16
        TABLET_PORTRAIT -> 2 to paddingMediumLarge20
        TABLET_LANDSCAPE -> 3 to  paddingMediumLarge20
        DESKTOP -> 3 to paddingMediumLarge20
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columnCount), // Change this base on screen size
        modifier = modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(contentPadding),
        verticalItemSpacing = contentPadding,
        contentPadding = PaddingValues(paddingMedium16),
    ) {
        items(
            items = notes,
            key = { note -> note.id }
        ) { noteUi ->
            NoteListItem(
               noteUi =  noteUi,
                deviceScreenType = deviceScreenType,
                onClick = {},
                onLongClick = {
                    onNoteLongClick(noteUi.id)
                },
                modifier = Modifier
            )
        }
    }
}


@Preview
@Composable
private fun NoteListPreview() {
    val noteUiPreview = remember {
        (1..15).map {
            noteUi.copy(
                id = it.toString()
            )
        }
    }
    NoteMarkTheme {
        NoteList(
            notes = noteUiPreview,
            deviceScreenType = MOBILE_PORTRAIT,
            onNoteLongClick = { /* Handle long click */ },
            modifier = Modifier
        )
    }
}