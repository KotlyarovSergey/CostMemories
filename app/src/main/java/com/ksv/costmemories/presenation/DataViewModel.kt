package com.ksv.costmemories.presenation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.PurchaseTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DataViewModel: ViewModel() {
    private val _purchases = MutableStateFlow<List<PurchaseTuple>>(emptyList())
    val purchase = _purchases.asStateFlow()

    init {
        Log.d("ksvlog", "vm init")
        viewModelScope.launch {
            val purchasesDao = Dependencies.getPurchasesDao()
            _purchases.value = purchasesDao.getAllAsTuple()
        }
    }

    fun addPurchase(purchase: Purchase){
        viewModelScope.launch {
            val purchasesDao = Dependencies.getPurchasesDao()
            purchasesDao.insert(purchase)
            _purchases.value = purchasesDao.getAllAsTuple()
//            val pd = Dependencies.getPurchasesDao()
//            pd.insert(purchase)
//            _purchases.value = pd.getAllAsTuple()

        }

    }
}