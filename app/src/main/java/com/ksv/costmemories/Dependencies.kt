package com.ksv.costmemories

import android.content.Context
import androidx.room.Room
import com.ksv.costmemories.data.AppDatabase

object Dependencies {
    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    private val appDatabase: AppDatabase by lazy{
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .build()
    }

    fun getShopsDao() = appDatabase.getShopsDao()
    fun getTitlesDao() = appDatabase.getTitlesDao()
    fun getProductsDao() = appDatabase.getProductsDao()
    fun getPurchasesDao() = appDatabase.getPurchasesDao()
}