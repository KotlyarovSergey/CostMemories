package com.ksv.costmemories.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.Title

@Dao
interface TitlesDao {
    @Query("SELECT * FROM titles")
    suspend fun getAllTitles(): List<Title>

    @Query("SELECT * FROM titles WHERE text = :titleText")
    suspend fun getTitle(titleText: String): Title

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inset(title: Title)

    @Update
    suspend fun update(title: Title)

    @Delete
    suspend fun delete(title: Title)

}