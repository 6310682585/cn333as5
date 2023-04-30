package com.example.mynotes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TagDao {

    @Query("SELECT * FROM TagDbModel")
    fun getAll(): LiveData<List<TagDbModel>>

    @Query("SELECT * FROM TagDbModel")
    fun getAllSync(): List<TagDbModel>

    @Query("SELECT * FROM TagDbModel WHERE id IN (:tagIds)")
    fun getTagsByIdsSync(tagIds: List<Long>): List<TagDbModel>

    @Query("SELECT * FROM TagDbModel WHERE id LIKE :id")
    fun findByIdSync(id: Long): TagDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tagDbModel: TagDbModel)

    @Insert
    fun insertAll(vararg tagDbModel: TagDbModel)

    @Query("DELETE FROM TagDbModel WHERE id LIKE :id")
    fun delete(id: Long)

    @Query("DELETE FROM TagDbModel WHERE id IN (:tagIds)")
    fun delete(tagIds: List<Long>)
}