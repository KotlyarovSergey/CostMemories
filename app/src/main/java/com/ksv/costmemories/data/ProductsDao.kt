package com.ksv.costmemories.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.Product

@Dao
interface ProductsDao {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

//    @Query("INSERT INTO groups(name) VALUES(:groupName)")
//    suspend fun insertUnique(groupName: String)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)
}