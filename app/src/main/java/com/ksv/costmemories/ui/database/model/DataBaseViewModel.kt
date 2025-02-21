package com.ksv.costmemories.ui.database.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.data.PurchasesDao
import com.ksv.costmemories.entity.EntityCounter
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
    private val titles = purchasesDao.titlesCounter()
        .map { titlesCounterToDbItems(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val shops = purchasesDao.shopsCounter()
        .map { shopsCounterToDbItems(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val products = purchasesDao.productsCounter()
        .map { productsCounterToDbItems(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val _items = MutableStateFlow<List<DbItem>>(emptyList())
    val items = _items.asStateFlow()

//    private val _checkedRadio = MutableStateFlow<DbItemType>(DbItemType.TITLE)
//    val checkedRadio = _checkedRadio.asStateFlow()

    private var _checkedRadio = DbItemType.PRODUCT
    val checkedRadio get() = _checkedRadio

    fun onCheckedChange(itemType: DbItemType){
        when(itemType){
            DbItemType.PRODUCT -> _items.value = products.value
            DbItemType.TITLE -> _items.value = titles.value
            DbItemType.SHOP -> _items.value = shops.value
        }
        _checkedRadio = itemType
    }

    private fun titlesCounterToDbItems(titles: List<EntityCounter>): List<DbItem>{
        return titles.map { title -> DbItem(title.id, title.title, title.count, DbItemType.TITLE) }
    }

    private fun shopsCounterToDbItems(shops: List<EntityCounter>): List<DbItem>{
        return shops.map { shop -> DbItem(shop.id, shop.title, shop.count, DbItemType.SHOP) }
    }

    private fun productsCounterToDbItems(products: List<EntityCounter>): List<DbItem>{
        return products.map { product -> DbItem(product.id, product.title, product.count, DbItemType.PRODUCT) }
    }
}