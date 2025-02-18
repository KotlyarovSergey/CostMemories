package com.ksv.costmemories.ui.home.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.data.PurchasesDao
import com.ksv.costmemories.entity.PurchaseTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    purchasesDao: PurchasesDao
) : ViewModel() {
    private val _purchasesDB = purchasesDao.getAllFlow()
        .onEach {
            _purchases.value = filter(it)
            _state.value = if (_purchases.value.isEmpty()) HomeState.Empty else HomeState.Normal
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _purchases = MutableStateFlow<List<PurchaseTuple>>(emptyList())
    val purchases = _purchases.asStateFlow()

    private val _state = MutableStateFlow<HomeState>(HomeState.Normal)
    val state = _state.asStateFlow()

    var filterSequence: String = ""
        set(value) {
            field = value.trim()
            filterPurchases()
        }

    fun onItemClick(id: Long) {
        _state.value = HomeState.EditPurchase(id)
    }

    fun onAddPurchaseClick() {
        _state.value = HomeState.AddPurchase
    }

    fun onAddPurchaseFragmentNavigate() {
        _state.value = if (purchases.value.isEmpty()) HomeState.Empty else HomeState.Normal
    }

    fun onEditPurchaseFragmentNavigate() {
        _state.value = if (purchases.value.isEmpty()) HomeState.Empty else HomeState.Normal
    }

//    fun onFilterTextChanged(ch: CharSequence) {
    fun onFilterTextChanged() {
//        Log.d("ksvlog", "onFilterChanged $filterSequence")
//        filterSequence = ch.toString()
        _purchases.value = filter(_purchasesDB.value)
        _state.value = if (_purchases.value.isEmpty()) HomeState.Empty else HomeState.Normal
    }

    private fun filter(purchases: List<PurchaseTuple>): List<PurchaseTuple> {
//        Log.d("ksvlog", "filter:\n\tpurchases: $purchases\n\tfilter: $filterSequence")
        val filtered = purchases
            .filter {
                it.title.contains(filterSequence, true) ||
                        it.product.contains(filterSequence, true)
            }

        return filtered
    }

    private fun filterPurchases(){
        _purchases.value = _purchasesDB.value.filter {
            it.title.contains(filterSequence, true) ||
            it.product.contains(filterSequence, true)
        }
        _state.value = if (_purchases.value.isEmpty()) HomeState.Empty else HomeState.Normal
    }

    private fun filterPurchasesAdvance(){
        val words = filterSequence.split(" ")
        
    }

}