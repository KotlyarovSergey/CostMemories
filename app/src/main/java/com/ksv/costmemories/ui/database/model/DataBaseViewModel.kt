package com.ksv.costmemories.ui.database.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.data.PurchasesDao
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.ui.database.entity.DbItem
import com.ksv.costmemories.ui.database.entity.DbItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DataBaseViewModel(
    purchasesDao: PurchasesDao
): ViewModel() {
    val titles = purchasesDao.getAllTitles()
        .map { titlesToDbItem(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val _items = MutableStateFlow<List<DbItem>>(emptyList())
    val items = _items.asStateFlow()

    fun onCheckedChange(itemType: DbItemType){

    }

    private fun titlesToDbItem(titles: List<Product>): List<DbItem>{
        return titles.map { title -> DbItem(title.id, title.title, 0, DbItemType.TITLE) }
    }
}