package com.selim.notesapp.model.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotesInformation")
data class Note(
    @PrimaryKey(autoGenerate = true)val id:Long =0,
    val title:String?,
    val note:String?,
    val date:String?,
    val userId:String?
):java.io.Serializable
