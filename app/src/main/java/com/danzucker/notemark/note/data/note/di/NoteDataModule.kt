package com.danzucker.notemark.note.data.note.di

import com.danzucker.notemark.note.data.note.OfflineFirstNoteRepository
import com.danzucker.notemark.note.data.note.local.RoomLocalNoteDataSource
import com.danzucker.notemark.note.data.note.network.KtorRemoteNoteDataSource
import com.danzucker.notemark.note.data.note.sync.SyncNoteWorkerScheduler
import com.danzucker.notemark.note.data.note.worker.CreateNoteWorker
import com.danzucker.notemark.note.data.note.worker.DeleteNoteWorker
import com.danzucker.notemark.note.data.note.worker.FetchNoteWorker
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.note.domain.note.local.LocalNoteDataSource
import com.danzucker.notemark.note.domain.note.network.RemoteNoteDataSource
import com.danzucker.notemark.note.domain.note.sync.SyncNoteScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val noteDataModule = module {
    // Workers

    workerOf(::FetchNoteWorker)
    workerOf(::CreateNoteWorker)
    workerOf(::DeleteNoteWorker)

    singleOf(::RoomLocalNoteDataSource).bind<LocalNoteDataSource>()
    singleOf(::KtorRemoteNoteDataSource).bind<RemoteNoteDataSource>()
    singleOf(::OfflineFirstNoteRepository).bind<NoteRepository>()
    singleOf(::SyncNoteWorkerScheduler).bind<SyncNoteScheduler>()
}