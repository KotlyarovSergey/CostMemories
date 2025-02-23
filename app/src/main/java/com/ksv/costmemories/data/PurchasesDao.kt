package com.ksv.costmemories.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.EntityCounter
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
            "INNER JOIN shops ON purchases.shop_id = shops.id "+
            "ORDER by purchases.id DESC")
    fun getAllAsTupleFlow(): Flow<List<PurchaseTuple>>


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
    suspend fun getPurchaseTuple(id: Long): PurchaseTuple

    @Query("SELECT * FROM purchases WHERE purchases.id=:id")
    suspend fun getPurchase(id: Long): Purchase

    @Query("SELECT purchases.id, date, cost, comment, product_groups.group_name AS product, " +
            "product_titles.title AS title, shops.shop_name AS shop_name, comment " +
            "FROM purchases " +
            "INNER JOIN product_groups ON purchases.product_id = product_groups.id " +
            "INNER JOIN product_titles ON purchases.title_id = product_titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id " +
            "WHERE purchases.id=:id")
    fun purchaseOnId(id: Long): Flow<PurchaseTuple?>

    @Insert
    suspend fun purchaseInsert(purchase: Purchase)

    @Update
    suspend fun purchaseUpdate(purchase: Purchase)

    @Delete
    suspend fun purchaseDelete(purchase: Purchase)



    //          SHOPS
    @Query("SELECT * FROM shops ORDER by shop_name")
    fun getAllShops(): Flow<List<Shop>>

    @Query("SELECT shops.id AS id, shop_name AS title, COUNT(purchases.id) AS count " +
            "FROM shops " +
            "LEFT JOIN purchases ON shops.id=purchases.shop_id " +
            "GROUP BY shops.id " +
            "ORDER BY shop_name")
    fun shopsCounter(): Flow<List<EntityCounter>>

    @Insert
    suspend fun shopInsert(shop: Shop): Long

    @Delete
    suspend fun shopDelete(shop: Shop)

    @Update
    suspend fun shopUpdate(shop: Shop)

    @Query("DELETE FROM shops WHERE id=:id")
    suspend fun shopDeleteId(id: Long)

    //          TITLES
    @Query("SELECT * FROM product_titles ORDER by title")
    fun getAllTitles(): Flow<List<Product>>

    @Query("SELECT product_titles.id AS id, title AS title, COUNT(purchases.id) AS count " +
            "FROM product_titles " +
            "LEFT JOIN purchases ON product_titles.id=purchases.title_id " +
            "GROUP BY product_titles.id " +
            "ORDER BY title")
    fun titlesCounter(): Flow<List<EntityCounter>>

    @Insert
    suspend fun titleInsert(product: Product): Long

    @Update
    suspend fun titleUpdate(product: Product)

    @Query("DELETE FROM product_titles WHERE id=:id")
    suspend fun titleDeleteId(id: Long)





    //          PRODUCTS
    @Query("SELECT * FROM product_groups ORDER by group_name")
    fun getAllProducts(): Flow<List<Group>>

    @Query("SELECT product_groups.id AS id, group_name AS title, COUNT(purchases.id) AS count " +
            "FROM product_groups " +
            "LEFT JOIN purchases ON product_groups.id=purchases.product_id " +
            "GROUP BY product_groups.id " +
            "ORDER BY group_name")
    fun productsCounter(): Flow<List<EntityCounter>>

    @Insert
    suspend fun productInsert(group: Group): Long

    @Update
    suspend fun productUpdate(group: Group)

    @Delete
    suspend fun productDelete(group: Group)

    @Query("DELETE FROM product_groups WHERE id=:id")
    suspend fun productDeleteId(id: Long)

}