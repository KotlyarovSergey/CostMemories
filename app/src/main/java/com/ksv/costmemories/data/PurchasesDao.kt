package com.ksv.costmemories.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.Group
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.entity.Shop
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasesDao {
    @Query("SELECT * FROM purchases")
    suspend fun getAll(): List<Purchase>

    @Query("SELECT " +
                "purchases.id, " +
                "date, " +
                "cost, " +
                "comment, " +
                "product_groups.group_name AS product, " +
                "product_titles.title AS title, " +
                "shops.shop_name AS shop_name, " +
                "comment " +
            "FROM purchases " +
            "INNER JOIN product_groups ON purchases.product_id = product_groups.id " +
            "INNER JOIN product_titles ON purchases.title_id = product_titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id")
    suspend fun getAllAsTuple(): List<PurchaseTuple>

    @Query("SELECT " +
            "purchases.id, " +
            "date, " +
            "cost, " +
            "comment, " +
            "product_groups.group_name AS product, " +
            "product_titles.title AS title, " +
            "shops.shop_name AS shop_name, " +
            "comment " +
            "FROM purchases " +
            "INNER JOIN product_groups ON purchases.product_id = product_groups.id " +
            "INNER JOIN product_titles ON purchases.title_id = product_titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id")
    fun getAllFlow(): Flow<List<PurchaseTuple>>


    @Query("SELECT " +
            "purchases.id, " +
            "date, " +
            "cost, " +
            "comment, " +
            "product_groups.group_name AS product, " +
            "product_titles.title AS title, " +
            "shops.shop_name AS shop_name, " +
            "comment " +
            "FROM purchases " +
            "INNER JOIN product_groups ON purchases.product_id = product_groups.id " +
            "INNER JOIN product_titles ON purchases.title_id = product_titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id " +
            "WHERE purchases.id=:id")
    suspend fun getPurchase(id: Long): PurchaseTuple

    @Query("SELECT purchases.id, date, cost, comment, product_groups.group_name AS product, " +
            "product_titles.title AS title, shops.shop_name AS shop_name, comment " +
            "FROM purchases " +
            "INNER JOIN product_groups ON purchases.product_id = product_groups.id " +
            "INNER JOIN product_titles ON purchases.title_id = product_titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id " +
            "WHERE purchases.id=:id")
    fun purchaseOnId(id: Long): Flow<PurchaseTuple>


    @Insert
    suspend fun insert(purchase: Purchase)

    @Update
    suspend fun update(purchase: Purchase)

    @Delete
    suspend fun delete(purchase: Purchase)

    //          SHOPS
    @Query("SELECT * FROM shops")
    fun getAllShops(): Flow<List<Shop>>

    @Insert
    suspend fun insertShop(shop: Shop): Long

    //          TITLES
    @Query("SELECT * FROM product_titles")
    fun getAllTitles(): Flow<List<Product>>

    @Insert
    suspend fun insertTitle(product: Product): Long

    //          PRODUCTS
    @Query("SELECT * FROM product_groups")
    fun getAllProducts(): Flow<List<Group>>

    @Insert
    suspend fun insertProduct(group: Group): Long
}