package com.vikas.notesforobvious.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.vikas.notesforobvious.R
import kotlinx.android.synthetic.main.activity_upsert_note.*

class UpsertNoteActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "com.vikas.notesforobvious.EXTRA_ID"
        const val EXTRA_TITLE = "com.vikas.notesforobvious.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.vikas.notesforobvious.EXTRA_DESCRIPTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upsert_note)


        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            edit_text_title.setText(intent.getStringExtra(EXTRA_TITLE))
            edit_text_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
        } else {
            title = "Add Note"
        }
    }


    private fun saveNote() {
        if (edit_text_title.text.toString().trim().isBlank() || edit_text_description.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Can not insert empty note!", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_TITLE, edit_text_title.text.toString())
            putExtra(EXTRA_DESCRIPTION, edit_text_description.text.toString())
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
            }
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveNote()
    }
}