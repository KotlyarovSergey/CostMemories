package com.ksv.costmemories.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.PurchaseTuple

@Dao
interface PurchasesDao {
    @Query("SELECT * FROM purchases")
    suspend fun getAll(): List<Purchase>

    @Query("SELECT purchases.id, date, cost, comment, products.name AS product_name, " +
            "titles.text AS title, shops.shop_name AS shop_name, comment FROM purchases " +
            "INNER JOIN products ON purchases.product_id = products.id " +
            "INNER JOIN titles ON purchases.title_id = titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id")
    suspend fun getAllAsTuple(): List<PurchaseTuple>


    @Insert
    suspend fun insert(purchase: Purchase)

    @Update
    suspend fun update(purchase: Purchase)

    @Delete
    suspend fun delete(purchase: Purchase)
}