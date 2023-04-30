package com.example.mynotes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phoneNum") val phoneNum: String,
    @ColumnInfo(name = "color_id") val colorId: Long,
    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean
) {
    companion object {
        val DEFAULT_PHONES = listOf(
            PhoneDbModel(1, "Anna", "08123456789", 1, 1, false),
            PhoneDbModel(2, "B", "08123456789", 2, 1, true),
            PhoneDbModel(3, "Zebra", "08123456789", 3, 1, false),
            PhoneDbModel(4, "D", "08123456789", 4, 1, false),
            PhoneDbModel(5, "E", "08123456789", 5, 1, false),
            PhoneDbModel(6, "F", "08123456789", 6, 2, false),
            PhoneDbModel(7, "G", "08123456789", 7, 2, false),
            PhoneDbModel(8, "H", "08123456789", 8, 2, false),
            PhoneDbModel(9, "I", "08123456789", 9, 3, false),
            PhoneDbModel(10, "J", "08123456789", 10, 3, false),
            PhoneDbModel(11, "K", "08123456789", 11, 4, false),
            PhoneDbModel(12, "L", "08123456789", 12, 4, false),

            )
    }
}