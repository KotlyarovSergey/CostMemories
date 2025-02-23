package com.ksv.costmemories.ui.database.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.R
import com.ksv.costmemories.data.PurchasesDao
import com.ksv.costmemories.entity.EntityCounter
import com.ksv.costmemories.ui.database.entity.DbItem
import com.ksv.costmemories.ui.database.entity.DbItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DataBaseViewModel(
    private val application: Application,
    private val purchasesDao: PurchasesDao
) : AndroidViewModel(application) {

    private val titles = purchasesDao.titlesCounter()
        .onEach {
            if (_checkedRadio == DbItemType.TITLE) {
                _items.value = titlesCounterToDbItems(it)
            }
        }
    private val shops = purchasesDao.shopsCounter()
        .onEach {
            if (_checkedRadio == DbItemType.SHOP) {
                _items.value = shopsCounterToDbItems(it)
            }
        }
    private val products = purchasesDao.productsCounter()
        .onEach {
            if (_checkedRadio == DbItemType.PRODUCT) {
                _items.value = productsCounterToDbItems(it)
            }
        }

    private val _items = MutableStateFlow<List<DbItem>>(emptyList())
    val items = _items.asStateFlow()

    private var _checkedRadio = DbItemType.PRODUCT
    val checkedRadio get() = _checkedRadio

    private val _state = MutableStateFlow<DbFragmentState>(DbFragmentState.Normal)
    val state = _state.asStateFlow()

    fun onCheckedChange(itemType: DbItemType) {
        when (itemType) {
            DbItemType.PRODUCT -> {
                viewModelScope.launch {
                    products.collect()
                }
            }

            DbItemType.TITLE -> {
                viewModelScope.launch {
                    titles.collect()
                }
            }

            DbItemType.SHOP -> {
                viewModelScope.launch {
                    shops.collect()
                }
            }
        }
        _checkedRadio = itemType
    }

    fun onItemApplyClick(item: DbItem, text: String) {
        if(item.text.compareTo(text) != 0) {
            if (itWillBeDuplicate(text)) {
                val msg = getReplaceConfirmMessage(item, text)
                _state.value = DbFragmentState.ReplaceConfirmRequest(item, text, msg)
            } else {
                updateItemOnDB(item, text)
                val msg = application.getString(R.string.dialog_replace_success_message, text)
                _state.value = DbFragmentState.ShowSuccessMessage(msg)
            }
        }
    }

    fun onItemDeleteClick(item: DbItem) {
        val msg =
            if (item.counter > 0)
                application.getString(R.string.dialog_item_delete_message, item.text, item.counter)
            else
                application.getString(R.string.dialog_empty_item_delete_message, item.text)

        _state.value = DbFragmentState.DeleteConfirmRequest(item.id, msg)
    }

    fun onItemDeleteConfirm(id: Long) {
        deleteItemFromDB(id)
    }

    fun onItemReplaceConfirm(item: DbItem, text: String) {
        val oldId = item.id
        val basedItem = items.value.firstOrNull { it.text.compareTo(text) == 0 }
        basedItem?.let {
            val newId = basedItem.id
            viewModelScope.launch {
                when(item.type){
                    DbItemType.PRODUCT -> {
                        purchasesDao.replaceProduct(oldId, newId)
                        purchasesDao.productDeleteId(oldId)
                    }
                    DbItemType.TITLE -> {
                        purchasesDao.replaceTitle(oldId, newId)
                        purchasesDao.titleDeleteId(oldId)
                    }
                    DbItemType.SHOP -> {
                        purchasesDao.replaceShop(oldId, newId)
                        purchasesDao.shopDeleteId(oldId)
                    }
                }
            }
        }
    }

    fun onItemMergeDeny(item: DbItem, text: String) {
        updateItemOnDB(item, text)
    }

    fun onConfirmDialogShow() {
        _state.value = DbFragmentState.Normal
    }



    private fun getReplaceConfirmMessage(item: DbItem, text: String): String{
        val type = when(item.type){
            DbItemType.PRODUCT -> application.getString(R.string.db_frag_product_caption)
            DbItemType.TITLE -> application.getString(R.string.db_frag_title_caption)
            DbItemType.SHOP -> application.getString(R.string.db_frag_shop_caption)
        }
        return application.getString(R.string.dialog_replace_confirm_message, type, text)
    }

    private fun itWillBeDuplicate(text: String): Boolean {
        items.value.forEach {
            if (it.text.compareTo(text) == 0) return true
        }
        return false
    }

    private fun updateItemOnDB(item: DbItem, text: String) {
        when (item.type) {
            DbItemType.PRODUCT -> updateProduct(item.id, text)
            DbItemType.TITLE -> updateTitle(item.id, text)
            DbItemType.SHOP -> updateShop(item.id, text)
        }
    }

    private fun updateTitle(id: Long, text: String) {
        viewModelScope.launch {
            val ondTitle = purchasesDao.getTitleOnId(id)
            ondTitle?.let {
                val newTitle = ondTitle.copy(title = text)
                purchasesDao.titleUpdate(newTitle)
            }
        }
    }

    private fun updateShop(id: Long, text: String) {
        viewModelScope.launch {
            val oldShop = purchasesDao.getShopOnId(id)
            oldShop?.let {
                val newShop = oldShop.copy(shop_name = text)
                purchasesDao.shopUpdate(newShop)
            }
        }
    }

    private fun updateProduct(id: Long, text: String) {
        viewModelScope.launch {
            val ondProduct = purchasesDao.getProductOnId(id)
            ondProduct?.let {
                val newProduct = ondProduct.copy(group = text)
                purchasesDao.productUpdate(newProduct)
            }
        }
    }

    private fun deleteItemFromDB(id: Long) {
        viewModelScope.launch {
            when (_checkedRadio) {
                DbItemType.PRODUCT -> purchasesDao.productDeleteId(id)
                DbItemType.TITLE -> purchasesDao.titleDeleteId(id)
                DbItemType.SHOP -> purchasesDao.shopDeleteId(id)
            }
        }
    }

    private fun titlesCounterToDbItems(titles: List<EntityCounter>): List<DbItem> {
        return titles.map { title -> DbItem(title.id, title.title, title.count, DbItemType.TITLE) }
    }

    private fun shopsCounterToDbItems(shops: List<EntityCounter>): List<DbItem> {
        return shops.map { shop -> DbItem(shop.id, shop.title, shop.count, DbItemType.SHOP) }
    }

    private fun productsCounterToDbItems(products: List<EntityCounter>): List<DbItem> {
        return products.map { product ->
            DbItem(
                product.id,
                product.title,
                product.count,
                DbItemType.PRODUCT
            )
        }
    }
}