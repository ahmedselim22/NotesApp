package com.selim.notesapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.selim.notesapp.R
import com.selim.notesapp.databinding.NoteItemLayoutBinding
import com.selim.notesapp.model.domain.Note
import com.selim.notesapp.ui.OnNoteItemClicked
import kotlin.random.Random

class NotesAdapter(private val listner:OnNoteItemClicked) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    private var originalList = ArrayList<Note>()
    private val diffUtil = object :DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id && oldItem.date ==newItem.date
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
    val differ= AsyncListDiffer(this,diffUtil)
    class NoteViewHolder(itemView: View):ViewHolder(itemView){
        val binding = NoteItemLayoutBinding.bind(itemView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout,parent,false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun searchNotes(searchQuery:String){
        if (originalList.isEmpty()) {
            originalList.addAll(differ.currentList.toList())
        }

        val filteredList = if (searchQuery.isNotEmpty()) {
            originalList.filter { item ->
                item.title?.toLowerCase()?.contains(searchQuery.toLowerCase()) == true ||
                        item.note?.toLowerCase()?.contains(searchQuery.toLowerCase()) == true
            }
        } else {
            originalList
        }

        differ.submitList(filteredList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        val colors = arrayOf(
            Color.parseColor("#FAEF5D"),
            Color.parseColor("#92C7CF"),
            Color.parseColor("#DC84F3"),
            Color.parseColor("#4F6F52"),
            Color.parseColor("#BF3131"),
            Color.parseColor("#65B741"),
            Color.parseColor("#AF2655"),
            Color.parseColor("#FF5B22")
        )
        val currentColor = colors[position%colors.size]
        holder.binding.apply {
            tvNoteItemTitle.text = currentNote.title
            tvNoteItemTitle.isSelected =true
            tvNoteItemContent.text = currentNote.note
            tvNoteItemDate.text = currentNote.date
            tvNoteItemDate.isSelected =true
            cardViewNoteItem.setCardBackgroundColor(currentColor)
            cardViewNoteItem.setOnClickListener {
                listner.onNoteItemClick(currentNote)
            }
        }
    }
}