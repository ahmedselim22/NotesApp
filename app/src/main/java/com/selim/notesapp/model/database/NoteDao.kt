package com.selim.notesapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.selim.notesapp.model.domain.Note

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note:Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT * FROM NotesInformation ORDER BY id ASC")
    fun getAllNotes():LiveData<List<Note>>

    @Query("UPDATE NotesInformation SET title=:title,note =:content WHERE id =:id")
    fun updateNote(id: Long?, title:String?, content:String?)

}