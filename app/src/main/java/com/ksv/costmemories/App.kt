package com.ksv.costmemories

import android.app.Application
import androidx.room.Room
import androidx.room.util.appendPlaceholders
import com.ksv.costmemories.data.AppDatabase

class App : Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        ).build()
    }
}