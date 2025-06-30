package com.danzucker.notemark.note.data.note.network

import com.danzucker.notemark.core.data.networking.delete
import com.danzucker.notemark.core.data.networking.get
import com.danzucker.notemark.core.data.networking.post
import com.danzucker.notemark.core.data.networking.put
import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.domain.util.map
import com.danzucker.notemark.note.data.note.mappers.toNote
import com.danzucker.notemark.note.data.note.mappers.toNoteDto
import com.danzucker.notemark.note.data.note.mappers.toNotes
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.model.Notes
import com.danzucker.notemark.note.domain.note.network.RemoteNoteDataSource
import io.ktor.client.HttpClient

private const val NOTES_ENDPOINT = "/api/notes"

class KtorRemoteNoteDataSource(
    private val httpClient: HttpClient
) : RemoteNoteDataSource {
    override suspend fun getNotes(): Result<Notes, DataError.Network> {
        return httpClient.get<NotesResponse>(
            route = NOTES_ENDPOINT
        ).map { noteDtos ->
            noteDtos.toNotes()
        }
    }

    override suspend fun postNote(note: Note): Result<Note, DataError.Network> {
        return httpClient.post<NoteDto, NoteDto>( // same request and response type
            route = NOTES_ENDPOINT,
            body = note.toNoteDto()
        ).map { noteDto ->
            noteDto.toNote()
        }
    }

    override suspend fun updateNote(note: Note): Result<Note, DataError.Network> {
        return httpClient.put<NoteDto, NoteDto>( // same request and response type
            route = NOTES_ENDPOINT,
            body = note.toNoteDto()
        ).map { noteDto ->
            noteDto.toNote()
        }
    }

    override suspend fun deleteNote(id: String): Result<Unit, DataError.Network> {
       return httpClient.delete(
            route = NOTES_ENDPOINT,
            queryParameters = mapOf("id" to id)
        )
    }
}