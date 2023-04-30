package com.example.mynotes.domain.model

import com.example.mynotes.database.TagDbModel

data class TagModel(
    val id: Long,
    val tagName: String,
) {
    companion object {
        val DEFAULT = with(TagDbModel.DEFAULT_TAG) { TagModel(id, tagName) }
    }
}