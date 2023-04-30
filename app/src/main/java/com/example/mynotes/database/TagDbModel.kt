package com.example.mynotes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "tagName") val tagName: String,
) {
    companion object {
        val DEFAULT_TAGS = listOf(
            TagDbModel(1, "no tag"),
            TagDbModel(2, "Phone"),
            TagDbModel(3, "Office"),
            TagDbModel(4, "Star"),
            )
        val DEFAULT_TAG = TagDbModel.DEFAULT_TAGS[0]
    }
}