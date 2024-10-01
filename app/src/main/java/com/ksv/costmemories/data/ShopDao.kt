package com.ksv.costmemories.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.Shop

@Dao
interface ShopDao {
    @Query("Select * FROM shops")
    suspend fun getAll(): List<Shop>
//    suspend fun getAll(): Flow<List<Shop>>

    @Insert
    suspend fun insert(shop: Shop)

    @Delete
    suspend fun delete(shop: Shop)

    @Update
    suspend fun update(shop: Shop)
}