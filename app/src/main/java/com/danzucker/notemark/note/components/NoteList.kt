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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMedium16
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMediumLarge
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.*
import com.danzucker.notemark.note.models.NoteUi
import com.danzucker.notemark.note.preview.NotePreviewModel.noteUi

@Composable
fun NoteList(
    notes: List<NoteUi>,
    deviceScreenType: DeviceScreenType,
    modifier: Modifier = Modifier,
) {
    val (columnCount, contentPadding) = when (deviceScreenType) {
        MOBILE_PORTRAIT -> 2 to paddingMedium16
        MOBILE_LANDSCAPE -> 3 to paddingMedium16
        TABLET_PORTRAIT -> 2 to paddingMediumLarge
        TABLET_LANDSCAPE -> 3 to  paddingMediumLarge
        DESKTOP -> 3 to paddingMediumLarge
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columnCount), // Change this base on screen size
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(contentPadding),
        verticalItemSpacing = paddingMedium16,
        horizontalArrangement = Arrangement.spacedBy(paddingMedium16),
    ) {
        items(
            items = notes,
            key = { note -> note.id }
        ) { noteUi ->
            NoteListItem(
               noteUi =  noteUi,
                onClick = { /* Handle note click */ },
                modifier = Modifier.padding(paddingMedium16)
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
                id = it
            )
        }
    }
    NoteMarkTheme {
        NoteList(
            notes = noteUiPreview,
            deviceScreenType = MOBILE_PORTRAIT,
            modifier = Modifier
        )
    }
}