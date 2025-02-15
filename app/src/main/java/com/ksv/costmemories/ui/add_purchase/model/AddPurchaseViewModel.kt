package com.ksv.costmemories.ui.add_purchase.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.data.PurchasesDao
import com.ksv.costmemories.entity.Group
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.util.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class AddPurchaseViewModel(
    private val purchasesDao: PurchasesDao
): ViewModel() {
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

    var date: String = getToday()
    var product: String = ""
    var title: String = ""
    var shop: String = ""
    var cost: String = ""
    var comment: String = ""

    private val _state = MutableStateFlow<AddState>(AddState.Normal)
    val state = _state.asStateFlow()



    fun onAddClick(){
        if(hasNoErrors()) {
            addPurchaseToDB()
            _state.value = AddState.Finish
        }
    }

    fun onDateClick(){
        _state.value = AddState.SetDate
    }

    fun onHomeFragmentNavigate(){
        _state.value = AddState.Normal
    }

    fun onDateDialogOpen(){
        _state.value = AddState.Normal
    }



    private fun hasNoErrors(): Boolean{
        return if(product.isBlank()){
            _state.value = AddState.Error("Укажите продукт")
            false
        } else if (title.isBlank()){
            _state.value = AddState.Error("Заполените название продукта")
            false
        } else if (shop.isBlank()){
            _state.value = AddState.Error("Укажите место покупки")
            false
        } else if (cost.toIntOrNull() == null){
            _state.value = AddState.Error("Укажите цену")
            false
        } else {
            true
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

    private fun getToday(): String {
        val todayInMills = Calendar.getInstance().timeInMillis
        val todayString = DateUtils.dateFromMillsToString(todayInMills)
        return todayString
    }

}