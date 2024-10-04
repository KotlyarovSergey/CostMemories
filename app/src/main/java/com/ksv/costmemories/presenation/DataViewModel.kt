package com.ksv.costmemories.presenation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.PurchaseTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DataViewModel: ViewModel() {
    private val purchasesDao = Dependencies.getPurchasesDao()
    private val _purchases = MutableStateFlow<List<PurchaseTuple>>(emptyList())
    val purchase = _purchases.asStateFlow()

    init {
        viewModelScope.launch {
            _purchases.value = purchasesDao.getAllAsTuple()
        }
    }

    fun addPurchase(purchase: Purchase){
        viewModelScope.launch {
            purchasesDao.insert(purchase)
            _purchases.value = purchasesDao.getAllAsTuple()
        }

    }
}