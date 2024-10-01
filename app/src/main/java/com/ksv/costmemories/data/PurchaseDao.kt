package com.ksv.costmemories.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.Purchase

@Dao
interface PurchaseDao {
    @Query("SELECT * FROM purchases")
    suspend fun getAll(): List<Purchase>

    @Delete
    suspend fun delete(purchase: Purchase)

    @Update
    suspend fun update(purchase: Purchase)

    @Insert
    suspend fun insert(purchase: Purchase)
}