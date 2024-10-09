package com.ksv.costmemories.presenation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class DataViewModel: ViewModel() {
    private val _purchases = MutableStateFlow<List<PurchaseTuple>>(emptyList())
    val purchase = _purchases.asStateFlow()
    private val _shops = MutableStateFlow<List<Shop>>(emptyList())
    val shops = _shops.asStateFlow()
    private val _titles = MutableStateFlow<List<Title>>(emptyList())
    val titles = _titles.asStateFlow()
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _state = MutableStateFlow<EditState>(EditState.Normal)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = EditState.Waiting
            val purchasesDao = Dependencies.getPurchasesDao()
            _purchases.value = purchasesDao.getAllAsTuple()
            val shopsDao = Dependencies.getShopsDao()
            _shops.value = shopsDao.getAllShops()
            val titleDao = Dependencies.getTitlesDao()
            _titles.value = titleDao.getAllTitles()
            val productsDao = Dependencies.getProductsDao()
            _products.value = productsDao.getAllProducts()
            _state.value = EditState.Normal
        }
    }

    fun checkUserInput(userInputSet: UserInputSet): InputCheckResult{
        if(userInputSet.date.isBlank())
            return InputCheckResult.EMPTY_DATE
        if(userInputSet.title.isBlank())
            return InputCheckResult.EMPTY_TITLE
        if(userInputSet.cost.isBlank())
            return InputCheckResult.EMPTY_COST

        return InputCheckResult.OK
    }

    fun fillDb(userInputSet: UserInputSet){

        viewModelScope.launch {
            val date = userInputSet.date
            val cost = userInputSet.cost.toInt()
            val comment = userInputSet.comment.ifBlank { null }

            // для Product, Shop и Title
            // если новые добавить в БД, потом получить из БД его id
            val productId = getProductId(userInputSet.product)
            val titleId = getTitleId(userInputSet.title)
            val shopId = getShopId(userInputSet.shop)

            val purchase = Purchase(
                date = date,
                productId = productId,
                titleId = titleId,
                shopId = shopId,
                cost = cost,
                comment = comment
            )
            val purchasesDao = Dependencies.getPurchasesDao()
            purchasesDao.insert(purchase)
            _purchases.value = purchasesDao.getAllAsTuple()
        }
    }

    private suspend fun getProductId(productName: String): Int {
        return doesProductHaveId(productName) ?: insertProductInDbAndGetId(productName)
    }

    private fun doesProductHaveId(productName: String): Int?{
        _products.value.forEach { product ->
            if(product.name == productName){
                return product.id
            }
        }
        return null
    }

    private suspend fun insertProductInDbAndGetId(productName: String): Int{
        val productsDao = Dependencies.getProductsDao()
        productsDao.insert(Product(name = productName))
        _products.value = productsDao.getAllProducts()
        return productsDao.getProductId(productName)
    }

    private suspend fun getTitleId(titleText: String): Int{
        return doesTitleHaveId(titleText) ?: insertTitleInDbAndGetId(titleText)
    }

    private fun doesTitleHaveId(titleText: String): Int?{
        _titles.value.forEach { title ->
            if(title.text == titleText){
                return title.id
            }
        }
        return null
    }

    private suspend fun insertTitleInDbAndGetId(titleText: String): Int{
        val titlesDao = Dependencies.getTitlesDao()
        titlesDao.inset(Title(text = titleText))
        _titles.value = titlesDao.getAllTitles()
        return titlesDao.getTitleId(titleText)
    }

    private suspend fun getShopId(shopName: String): Int{
        return doesShopHaveId(shopName) ?: insertShopInDbAndGetId(shopName)
    }

    private fun doesShopHaveId(shopName: String): Int?{
        _shops.value.forEach { shop ->
            if(shop.shop_name == shopName)
                return shop.id
        }
        return null
    }

    private suspend fun insertShopInDbAndGetId(shopName: String): Int{
        val shopDao = Dependencies.getShopsDao()
        shopDao.insert(Shop(shop_name = shopName))
        _shops.value = shopDao.getAllShops()
        return shopDao.getShopId(shopName)
    }
}