package com.danzucker.notemark.note.presentation.createnote

data class CreateNoteState(
    val id: String = "",
   val titleText: String = "",
    val contentText: String = ""
)