package com.selim.notesapp.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.selim.notesapp.databinding.ActivityAddNoteBinding
import com.selim.notesapp.model.domain.Note
import com.selim.notesapp.utils.Constants
import java.util.Date

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private var isUpdate =false
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        setup()
    }

    private fun setup() {
        val id:Long?= doForUpdate()
        val intent = Intent()
        binding.ivAddNoteIconAdd.setOnClickListener {
            val title =binding.etAddNoteTitle.text.toString()
            val content =binding.etAddNoteContent.text.toString()
            val date = Date().toString()
            val uid = auth.currentUser?.uid
            var note:Note
            if (isUpdate){
                 note= Note(id = id!!,note=content, title = title,date=date, userId = uid)
            }else{
                note = Note(title=title, note = content, date = date, userId = uid)
            }
            intent.putExtra(Constants.NOTE_STRING,note)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        binding.ivAddNoteIconBack.setOnClickListener {
            setResult(Activity.RESULT_CANCELED,intent)
            finish()
        }
    }

    private fun doForUpdate() :Long?{
        try {
            val note: Note = intent.getSerializableExtra(Constants.NOTE_STRING) as Note
            if (note != null) {
                isUpdate = true
                binding.etAddNoteTitle.setText(note.title)
                binding.etAddNoteContent.setText(note.note)
                return note.id
            }
        }catch (e:Exception){
            print(e.printStackTrace())
        }
        return null
    }

}