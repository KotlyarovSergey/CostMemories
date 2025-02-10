package com.ksv.costmemories.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ksv.costmemories.entity.Group
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Product


@Database(entities = [Group::class, Product::class, Shop::class, Purchase::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getShopsDao(): ShopsDao
    abstract fun getTitlesDao(): TitlesDao
    abstract fun getProductsDao(): ProductsDao
    abstract fun getPurchasesDao(): PurchasesDao
}