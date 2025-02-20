package com.ksv.costmemories.ui.edit_purchase.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.data.PurchasesDao
import com.ksv.costmemories.entity.Group
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.entity.Shop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditPurchaseViewModel(
    private val purchasesDao: PurchasesDao,
    purchaseId: Long
): ViewModel() {
    val purchaseTuple = purchasesDao.purchaseOnId(purchaseId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
//            initialValue = PurchaseTuple.EMPTY
        )

    val shops = purchasesDao.getAllShops()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    val products = purchasesDao.getAllProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    val titles = purchasesDao.getAllTitles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow<EditState>(EditState.Normal)
    val state = _state.asStateFlow()

    var date: String = ""
    var product: String = ""
    var title: String = ""
    var shop: String = ""
    var cost: String = ""
    var comment: String = ""

    fun onDateClick(){
        _state.value = EditState.SetDate
    }

    fun onSaveClick(){
        if(hasNoErrors()){
            if(purchaseTuple.value == null)
                addPurchaseToDB()
            else
                updatePurchaseOnDB()
            _state.value = EditState.Finish
        }
    }



    fun onDeleteClick(){
        // ask to Delete
        _state.value = EditState.Delete
        //onDeleteConfirm()
    }

    fun onDeleteConfirm(){
        purchaseTuple.value?.let {
            CoroutineScope(Dispatchers.Default).launch {
            //viewModelScope.launch {
                val purchase = purchasesDao.getPurchase(purchaseTuple.value!!.id)
                purchasesDao.delete(purchase)
            }
        }
        _state.value = EditState.Finish
    }

    fun onHomeNavigate(){
        _state.value = EditState.Normal
    }

    fun onErrorMsgShow(){
        _state.value = EditState.Normal
    }

    fun onDeleteConfirmDialogDismiss(){
        _state.value = EditState.Normal
    }

    fun onDateDialogOpen(){
        _state.value = EditState.Normal
    }



    private fun hasNoErrors(): Boolean{
        return if(date.isBlank()){
            _state.value = EditState.Error("Выберите дату покупки")
            false
        } else if(product.isBlank()){
            _state.value = EditState.Error("Укажите продукт")
            false
        } else if (title.isBlank()){
            _state.value = EditState.Error("Заполените название продукта")
            false
        } else if (shop.isBlank()){
            _state.value = EditState.Error("Укажите место покупки")
            false
        } else if (cost.toIntOrNull() == null){
            _state.value = EditState.Error("Укажите цену")
            false
        } else {
            true
        }
    }

    private fun updatePurchaseOnDB(){
//      viewModelScope.launch {
        purchaseTuple.value?.let {
            CoroutineScope(Dispatchers.Default).launch {
                val id = purchaseTuple.value!!.id
                val shopId = getShopId()
                val titleId = getTitleId()
                val productId = getProductId()
                val purchase = Purchase(
                    id = id,
                    date = date,
                    productId = productId,
                    shopId = shopId,
                    titleId = titleId,
                    comment = comment,
                    cost = cost.toInt(),
                )
                purchasesDao.update(purchase)
            }
        }
    }

    private fun addPurchaseToDB(){
//      viewModelScope.launch {
        CoroutineScope(Dispatchers.Default).launch {
            val shopId = getShopId()
            val titleId = getTitleId()
            val productId = getProductId()
            val purchase = Purchase(
                date = date,
                productId = productId,
                shopId = shopId,
                titleId = titleId,
                comment = comment,
                cost = cost.toInt(),
            )
            purchasesDao.insert(purchase)
        }
    }

    private suspend fun getShopId():Long{
        val shopTrim = shop.trim()
        val correlateShop = shops.value.find { it.shop_name == shopTrim }
        return correlateShop?.id ?: purchasesDao.insertShop(Shop(shop_name = shopTrim))
    }

    private suspend fun getProductId():Long{
        val productTrim = product.trim()
        val correlateProduct = products.value.find { it.group == productTrim }
        return correlateProduct?.id ?: purchasesDao.insertProduct(Group(group = productTrim))
    }

    private suspend fun getTitleId():Long{
        val titleTrim = title.trim()
        val correlateTitle = titles.value.find { it.title == titleTrim }
        return correlateTitle?.id ?: purchasesDao.insertTitle(Product(title = titleTrim))
    }
}