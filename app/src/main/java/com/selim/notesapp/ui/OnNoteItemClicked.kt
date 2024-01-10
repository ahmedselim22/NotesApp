package com.selim.notesapp.ui

import com.selim.notesapp.model.domain.Note

interface OnNoteItemClicked {
    fun onNoteItemClick(note: Note)
}