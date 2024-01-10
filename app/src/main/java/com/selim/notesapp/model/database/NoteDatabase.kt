package com.selim.notesapp.model.database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import com.selim.notesapp.model.domain.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase :RoomDatabase(){

    abstract fun noteDao():NoteDao
    companion object {
        private var INSTANCE: RoomDatabase? = null
        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "myNoteDatabase"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return INSTANCE as NoteDatabase
        }
    }
}