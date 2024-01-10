package com.selim.notesapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.selim.notesapp.model.database.NoteDatabase
import com.selim.notesapp.model.domain.Note
import com.selim.notesapp.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteViewModel(private val database: NoteDatabase):ViewModel() {
    var notesLiveData: LiveData<List<Note>>
    val searchedNotesLiveData = MutableLiveData<List<Note>>()
    private val repository = NoteRepository(database.noteDao())

    init {
        notesLiveData =repository.allNotes
    }


    fun addNewNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewNote(note)
        }
    }
    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }
    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }
//    fun searchNotes(searchQuery: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            delay(500)
//            val searchedNotes= repository.searchNotes(searchQuery)
//            searchedNotesLiveData.postValue(searchedNotes)
//        }
//    }
}