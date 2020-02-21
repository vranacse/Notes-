package com.vikas.notesforobvious.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vikas.notesforobvious.R
import com.vikas.notesforobvious.adapter.NoteAdapter
import com.vikas.notesforobvious.data.Note
import com.vikas.notesforobvious.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAddNote.setOnClickListener {
            startActivityForResult(
                Intent(this, UpsertNoteActivity::class.java),
                ADD_NOTE_REQUEST
            )
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        var adapter = NoteAdapter()

        recycler_view.adapter = adapter

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        noteViewModel.getAllNotes().observe(this, Observer<List<Note>> {
            adapter.submitList(it)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT.or(
                ItemTouchHelper.RIGHT
            )
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(baseContext, "Note Deleted!", Toast.LENGTH_SHORT).show()
            }
        }
        ).attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                var intent = Intent(baseContext, UpsertNoteActivity::class.java)
                intent.putExtra(UpsertNoteActivity.EXTRA_ID, note.id)
                intent.putExtra(UpsertNoteActivity.EXTRA_TITLE, note.title)
                intent.putExtra(UpsertNoteActivity.EXTRA_DESCRIPTION, note.description)

                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })

        button_delete_all.setOnClickListener {
            noteViewModel.deleteAllNotes()
            Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val newNote = Note(
                data!!.getStringExtra(UpsertNoteActivity.EXTRA_TITLE),
                data!!.getStringExtra(UpsertNoteActivity.EXTRA_DESCRIPTION)
            )
            noteViewModel.insert(newNote)

            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(UpsertNoteActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(this, "Could not update! Error!", Toast.LENGTH_SHORT).show()
            }

            val updateNote = Note(
                data!!.getStringExtra(UpsertNoteActivity.EXTRA_TITLE),
                data.getStringExtra(UpsertNoteActivity.EXTRA_DESCRIPTION)
            )
            updateNote.id = data.getIntExtra(UpsertNoteActivity.EXTRA_ID, -1)
            noteViewModel.update(updateNote)

        } else {
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show()
        }


    }
}

