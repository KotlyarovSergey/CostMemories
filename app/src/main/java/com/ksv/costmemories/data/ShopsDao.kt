package com.ksv.costmemories.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.Shop

@Dao
interface ShopsDao {
    @Query("SELECT * FROM shops")
    suspend fun getAllShops(): List<Shop>

    @Query("SELECT id FROM shops WHERE shop=:shopName")
    suspend fun getShopId(shopName: String): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shop: Shop)

    @Update
    suspend fun update(shop: Shop)

    @Delete
    suspend fun delete(shop: Shop)
}