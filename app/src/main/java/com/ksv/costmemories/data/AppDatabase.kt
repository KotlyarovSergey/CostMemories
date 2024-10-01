package com.ksv.costmemories.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.Shop


@Database(entities = [Shop::class, Product::class, Purchase::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun shopDao(): ShopDao
    abstract fun productDao(): ProductDao
    abstract fun purchaseDao(): PurchaseDao
}