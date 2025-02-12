package com.ksv.costmemories.ui.home.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.data.PurchasesDao
import com.ksv.costmemories.entity.PurchaseTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    purchasesDao: PurchasesDao
): ViewModel() {
    private val _purchases = purchasesDao.getAllFlow()
    val purchases = _purchases
        .map { filter(it) }
        .onEach {
            _state.value = if(it.isEmpty()) HomeState.Empty else HomeState.Normal
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow<HomeState>(HomeState.Normal)
    val state = _state.asStateFlow()


    private var _filterSequence: String = ""



    fun setFilter(filter: String){
        _filterSequence = filter
    }

    fun onItemClick(id: Long){

    }

    fun onAddPurchaseClick(){

    }



    private fun filter(purchases: List<PurchaseTuple>): List<PurchaseTuple>{
        val filtered = purchases
            .filter { it.title.contains(_filterSequence, true) ||
                    it.product.contains(_filterSequence, true) }

        return filtered
    }

}