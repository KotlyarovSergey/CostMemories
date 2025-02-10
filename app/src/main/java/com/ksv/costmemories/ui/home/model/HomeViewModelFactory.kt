package com.ksv.costmemories.ui.home.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ksv.costmemories.data.PurchasesDao

class HomeViewModelFactory(private val purchasesDao: PurchasesDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(purchasesDao) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}