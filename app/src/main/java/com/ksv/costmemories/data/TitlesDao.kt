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
    @Query("SELECT * FROM product_titles")
    suspend fun getAllTitles(): List<Title>

    @Query("SELECT * FROM product_titles WHERE title = :title")
    suspend fun getTitle(title: String): Title

    @Query("SELECT id FROM product_titles WHERE title = :title")
    suspend fun getTitleId(title: String): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inset(title: Title)

    @Update
    suspend fun update(title: Title)

    @Delete
    suspend fun delete(title: Title)

}