package com.vikas.notesforobvious.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(

    var title: String,

    var description: String

) {

    //does it matter if these are private or not?
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}