package com.selim.notesapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.selim.notesapp.adapters.NotesAdapter
import com.selim.notesapp.databinding.ActivityMainBinding
import com.selim.notesapp.model.database.NoteDatabase
import com.selim.notesapp.model.domain.Note
import com.selim.notesapp.ui.OnNoteItemClicked
import com.selim.notesapp.utils.Constants
import com.selim.notesapp.viewModels.NoteViewModel
import com.selim.notesapp.viewModels.NoteViewModelFactory

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() ,OnNoteItemClicked{

    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter :NotesAdapter
    private lateinit var viewModel: NoteViewModel
    private lateinit var database: NoteDatabase
    private lateinit var factory: NoteViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = NoteDatabase.getInstance(this)
        factory = NoteViewModelFactory(database)
        viewModel = ViewModelProvider(this,factory)[NoteViewModel::class.java]
        adapter = NotesAdapter(this)
        initRecyclerView()
        setup()
        swipeToDelete()
    }

    private fun swipeToDelete() {
        val itemTouchHelper = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currentNote = adapter.differ.currentList[position]
                viewModel.deleteNote(currentNote)
                Snackbar.make(binding.root,"Note Deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                ) {
                    viewModel.addNewNote(currentNote)
                    Toast.makeText(this@MainActivity, "saved again", Toast.LENGTH_SHORT).show()
                }.show()
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recyclerView)
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
    }


    private fun setup() {
        binding.fabMainAddNote.setOnClickListener {
            val intent = Intent(this@MainActivity,AddNoteActivity::class.java)
            startActivityForResult(intent,Constants.ADD_REQUEST_CODE)
        }
        observeNotes()
        binding.searchView.setOnQueryTextListener(object :OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!=null){
                    adapter.searchNotes(newText)
                }
                return true
            }

        })
        binding.ivMainLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            intent.putExtra(Constants.FROM_STRING,Constants.CAME_FROM_MAIN)
            startActivity(intent)
            finish()
        }

    }

    private fun observeNotes() {
        viewModel.notesLiveData.observe(this) { list ->
            list?.let {
                val newList = ArrayList<Note>()
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                for (item in list) {
                    if (item.userId?.equals(uid) == true) {
                        newList.add(item)
                    } else {
                        continue
                    }
                }
                adapter.differ.submitList(newList)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK && requestCode==Constants.ADD_REQUEST_CODE){
            val note = data!!.getSerializableExtra(Constants.NOTE_STRING) as Note
            if (note!=null){
                viewModel.addNewNote(note)
            }
        }
        else if (resultCode==Activity.RESULT_OK && requestCode==Constants.UPDATE_REQUEST_CODE){
            val note = data!!.getSerializableExtra(Constants.NOTE_STRING) as Note
            if (note!=null){
                viewModel.updateNote(note)
            }
        }
        else{
            Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNoteItemClick(note: Note) {
        val intent = Intent(this@MainActivity,AddNoteActivity::class.java)
        intent.putExtra("note",note)
        startActivityForResult(intent,Constants.UPDATE_REQUEST_CODE)
    }

}