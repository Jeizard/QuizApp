package com.jeizard.quizapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BaseDao<E> {
    @Insert
    fun insert(entity: E): Long

    @Update
    fun update(entity: E)

    @Delete
    fun delete(entity: E)

    @Query("")
    fun deleteAll()

    @Query("")
    fun getAll(): List<E>
}
