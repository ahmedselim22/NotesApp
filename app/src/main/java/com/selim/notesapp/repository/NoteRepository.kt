package com.selim.notesapp.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.selim.notesapp.model.database.NoteDao
import com.selim.notesapp.model.domain.Note

class NoteRepository(private val dao:NoteDao) {
    val allNotes :LiveData<List<Note>> = dao.getAllNotes()
    suspend fun addNewNote(note: Note){
        dao.insertNote(note)
    }
    suspend fun deleteNote(note: Note){
        dao.deleteNote(note)
    }
    suspend fun updateNote(note: Note){
        return dao.updateNote(note.id,note.title,note.note)
    }
}