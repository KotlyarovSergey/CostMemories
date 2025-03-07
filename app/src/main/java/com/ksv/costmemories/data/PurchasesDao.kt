package com.ksv.costmemories.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ksv.costmemories.entity.EntityCounter
import com.ksv.costmemories.entity.Group
import com.ksv.costmemories.entity.Title
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
                "products.product AS product, " +
                "titles.title AS title, " +
            "shops.shop AS shop, " +
                "comment " +
            "FROM purchases " +
            "INNER JOIN products ON purchases.product_id = products.id " +
            "INNER JOIN titles ON purchases.title_id = titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id")
    suspend fun getAllAsTuple(): List<PurchaseTuple>


    @Query("SELECT " +
            "purchases.id, " +
            "date, " +
            "cost, " +
            "comment, " +
            "products.product AS product, " +
            "titles.title AS title, " +
            "shops.shop AS shop, " +
            "comment " +
            "FROM purchases " +
            "INNER JOIN products ON purchases.product_id = products.id " +
            "INNER JOIN titles ON purchases.title_id = titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id "+
            "ORDER by purchases.id DESC")
    fun getAllAsTupleFlow(): Flow<List<PurchaseTuple>>


    @Query("SELECT " +
            "purchases.id, " +
            "date, " +
            "cost, " +
            "comment, " +
            "products.product AS product, " +
            "titles.title AS title, " +
            "shops.shop AS shop, " +
            "comment " +
            "FROM purchases " +
            "INNER JOIN products ON purchases.product_id = products.id " +
            "INNER JOIN titles ON purchases.title_id = titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id " +
            "WHERE purchases.id=:id")
    suspend fun getPurchaseTuple(id: Long): PurchaseTuple

    @Query("SELECT * FROM purchases WHERE purchases.id=:id")
    suspend fun getPurchase(id: Long): Purchase

    @Query("SELECT purchases.id, date, cost, comment, " +
            "products.product AS product, " +
            "titles.title AS title, " +
            "shops.shop AS shop, comment " +
            "FROM purchases " +
            "INNER JOIN products ON purchases.product_id = products.id " +
            "INNER JOIN titles ON purchases.title_id = titles.id " +
            "INNER JOIN shops ON purchases.shop_id = shops.id " +
            "WHERE purchases.id=:id")
    fun purchaseOnId(id: Long): Flow<PurchaseTuple?>

    @Insert
    suspend fun purchaseInsert(purchase: Purchase)

    @Update
    suspend fun purchaseUpdate(purchase: Purchase)

    @Query("UPDATE purchases " +
            "SET product_id=:newId " +
            "WHERE product_id=:oldId")
    suspend fun replaceProduct(oldId: Long, newId: Long)

    @Query("UPDATE purchases " +
            "SET title_id=:newId " +
            "WHERE title_id=:oldId")
    suspend fun replaceTitle(oldId: Long, newId: Long)

    @Query("UPDATE purchases " +
            "SET shop_id=:newId " +
            "WHERE shop_id=:oldId")
    suspend fun replaceShop(oldId: Long, newId: Long)


    @Delete
    suspend fun purchaseDelete(purchase: Purchase)



    //                  SHOPS

    @Query("SELECT * FROM shops ORDER by shop")
    fun getAllShops(): Flow<List<Shop>>

    @Query(
        "SELECT shops.id AS id, shop AS title, COUNT(purchases.id) AS count " +
            "FROM shops " +
            "LEFT JOIN purchases ON shops.id=purchases.shop_id " +
            "GROUP BY shops.id " +
                "ORDER BY UPPER(shop)"
    )
    fun shopsCounter(): Flow<List<EntityCounter>>

    @Query ("SELECT * FROM shops WHERE id=:id")
    suspend fun getShopOnId(id: Long): Shop?

    @Query ("SELECT * FROM shops WHERE shop=:shopName")
    suspend fun getShopByName(shopName: String): Shop?

    @Insert
    suspend fun shopInsert(shop: Shop): Long

    @Update
    suspend fun shopUpdate(shop: Shop)

    @Delete
    suspend fun shopDelete(shop: Shop)

    @Query("DELETE FROM shops WHERE id=:id")
    suspend fun shopDeleteId(id: Long)



    //                  TITLES
    @Query("SELECT * FROM titles ORDER by title")
    fun getAllTitles(): Flow<List<Title>>

    @Query("SELECT titles.id AS id, title AS title, COUNT(purchases.id) AS count " +
            "FROM titles " +
            "LEFT JOIN purchases ON titles.id=purchases.title_id " +
            "GROUP BY titles.id " +
            "ORDER BY UPPER(title)")
    fun titlesCounter(): Flow<List<EntityCounter>>

    @Query ("SELECT * FROM titles WHERE id=:id")
    suspend fun getTitleOnId(id: Long): Title?

    @Query ("SELECT * FROM titles WHERE title=:title")
    suspend fun getTitleByName(title: String): Title?

    @Insert
    suspend fun titleInsert(title: Title): Long

    @Update
    suspend fun titleUpdate(title: Title)

    @Query("DELETE FROM titles WHERE id=:id")
    suspend fun titleDeleteId(id: Long)





    //          PRODUCTS
    @Query("SELECT * FROM products ORDER by product")
    fun getAllProducts(): Flow<List<Group>>

    @Query("SELECT products.id AS id, product AS title, COUNT(purchases.id) AS count " +
            "FROM products " +
            "LEFT JOIN purchases ON products.id=purchases.product_id " +
            "GROUP BY products.id " +
            "ORDER BY UPPER(product)")
    fun productsCounter(): Flow<List<EntityCounter>>

    @Query ("SELECT * FROM products WHERE id=:id")
    suspend fun getProductOnId(id: Long): Group?

    @Query ("SELECT * FROM products WHERE product=:product")
    suspend fun getProductByName(product: String): Group?

    @Insert
    suspend fun productInsert(group: Group): Long

    @Update
    suspend fun productUpdate(group: Group)

    @Delete
    suspend fun productDelete(group: Group)

    @Query("DELETE FROM products WHERE id=:id")
    suspend fun productDeleteId(id: Long)

}