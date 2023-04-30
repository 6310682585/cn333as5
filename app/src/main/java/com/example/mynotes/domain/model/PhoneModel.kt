package com.example.mynotes.domain.model

const val NEW_PHONE_ID = -1L

data class PhoneModel(
    val id: Long = NEW_PHONE_ID, // This value is used for new notes
    val name: String = "",
    val phoneNum: String = "",
    val color: ColorModel = ColorModel.DEFAULT,
    val tag: TagModel = TagModel.DEFAULT
)