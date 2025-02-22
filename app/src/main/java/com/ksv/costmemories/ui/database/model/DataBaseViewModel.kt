package com.ksv.costmemories.ui.database.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.R
import com.ksv.costmemories.data.PurchasesDao
import com.ksv.costmemories.entity.EntityCounter
import com.ksv.costmemories.ui.database.entity.DbItem
import com.ksv.costmemories.ui.database.entity.DbItemType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataBaseViewModel(
    private val application: Application,
    private val purchasesDao: PurchasesDao
): AndroidViewModel(application) {

    private val titles = purchasesDao.titlesCounter()
        //.map { titlesCounterToDbItems(it) }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.Eagerly,
//            emptyList()
//        )
        .onEach {
            if(_checkedRadio == DbItemType.TITLE){
                _items.value = titlesCounterToDbItems(it)
            }
        }

    private val shops = purchasesDao.shopsCounter()
        //.map { shopsCounterToDbItems(it) }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.Eagerly,
//            emptyList()
//        )
        .onEach {
            if(_checkedRadio == DbItemType.SHOP){
                _items.value = titlesCounterToDbItems(it)
            }
        }

    private val products = purchasesDao.productsCounter()
        //.map { productsCounterToDbItems(it) }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.Eagerly,
//            emptyList()
//        )
        .onEach {
            if(_checkedRadio == DbItemType.PRODUCT){
                _items.value = titlesCounterToDbItems(it)
            }
        }


    private val _items = MutableStateFlow<List<DbItem>>(emptyList())
    val items = _items.asStateFlow()

    var itms = products.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    private var _checkedRadio = DbItemType.PRODUCT
    val checkedRadio get() = _checkedRadio

    private val _state = MutableStateFlow<DbFragmentState>(DbFragmentState.Normal)
    val state = _state.asStateFlow()

    fun onCheckedChange(itemType: DbItemType){
        when(itemType){
            DbItemType.PRODUCT -> {
                //_items.value = products.value
                itms = products.stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    emptyList()
                )


            }
            DbItemType.TITLE -> {
                //_items.value = titles.value
                itms = titles.stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    emptyList()
                )
            }
            DbItemType.SHOP -> {
                //_items.value = shops.value
                itms = shops.stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    emptyList()
                )
            }
        }
        _checkedRadio = itemType
    }

    fun onItemDeleteClick(item: DbItem){
        val msg = if(item.counter > 0)
            application.getString(R.string.dialog_item_delete_message, item.text, item.counter)
        else
            application.getString(R.string.dialog_empty_item_delete_message, item.text)

        _state.value = DbFragmentState.ConfirmRequest(item.id, msg)
    }

    fun onItemDeleteConfirm(id: Long){
        when(_checkedRadio){
            DbItemType.PRODUCT -> { }
            DbItemType.TITLE -> deleteTitle(id)
            DbItemType.SHOP -> { }
        }
    }

    fun onConfirmDialogShow(){
        _state.value = DbFragmentState.Normal
    }


    private fun deleteTitle(id: Long){
        viewModelScope.launch {
            purchasesDao.titleDeleteId(id)
        }
    }

    private fun deleteItemSuspend(deleteFunction: (Long) -> Unit, id: Long){
        viewModelScope.launch {
            deleteFunction(id)
        }
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