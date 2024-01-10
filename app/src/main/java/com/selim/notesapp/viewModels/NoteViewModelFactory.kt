package com.selim.notesapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.selim.notesapp.model.database.NoteDatabase

class NoteViewModelFactory(private val database: NoteDatabase):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return NoteViewModel(database) as T
    }
}