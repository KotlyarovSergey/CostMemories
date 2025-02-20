package com.ksv.costmemories.ui.database.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ksv.costmemories.data.PurchasesDao

class DataBaseViewModelAdapter(private val purchasesDao: PurchasesDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataBaseViewModel::class.java))
            return DataBaseViewModel(purchasesDao) as T
        else
            throw IllegalArgumentException("unknown ViewModel class")
    }
}