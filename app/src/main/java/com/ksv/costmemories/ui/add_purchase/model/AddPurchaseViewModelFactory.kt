package com.ksv.costmemories.ui.add_purchase.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ksv.costmemories.data.PurchasesDao

class AddPurchaseViewModelFactory(private val purchasesDao: PurchasesDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddPurchaseViewModel::class.java))
            return AddPurchaseViewModel(purchasesDao) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}