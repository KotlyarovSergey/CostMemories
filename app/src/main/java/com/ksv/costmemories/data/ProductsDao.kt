package com.ksv.costmemories.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.Group

@Dao
interface ProductsDao {
    @Query("SELECT * FROM product_groups")
    suspend fun getAllProducts(): List<Group>

    @Query("SELECT * FROM product_groups WHERE group_name=:group")
    suspend fun getProduct(group: String): Group

    @Query("SELECT id FROM product_groups WHERE group_name=:group")
    suspend fun getProductId(group: String): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Group)

//    @Query("INSERT INTO groups(name) VALUES(:groupName)")
//    suspend fun insertUnique(groupName: String)

    @Update
    suspend fun update(product: Group)

    @Delete
    suspend fun delete(product: Group)
}