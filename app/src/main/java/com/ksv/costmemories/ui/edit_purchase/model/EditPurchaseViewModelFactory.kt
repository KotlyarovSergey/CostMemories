package com.ksv.costmemories.ui.edit_purchase.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ksv.costmemories.data.PurchasesDao

class EditPurchaseViewModelFactory(
    private val purchasesDao: PurchasesDao,
    private val id: Long
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditPurchaseViewModel::class.java))
            return EditPurchaseViewModel(purchasesDao, id) as T
        throw IllegalArgumentException("Unknown view model")
    }
}
