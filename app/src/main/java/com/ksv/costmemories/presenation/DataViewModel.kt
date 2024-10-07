package com.ksv.costmemories.presenation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.E


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
        //Log.d("ksvlog", "vm init")
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

    fun processUserInput(context: Context, userInputSet: UserInputSet){
        val checkResult = checkUserInput(context, userInputSet)
        if (checkResult is EditState.Error){
            _state.value = checkResult
        } else if (checkResult is EditState.Normal){
            fillDb(userInputSet)
        }
    }

    private fun checkUserInput(context: Context, userInputSet: UserInputSet) : EditState{
        if(userInputSet.date.isBlank())
            return EditState.Error(context.getString(R.string.date_input_error))
        if(userInputSet.title.isBlank())
            return EditState.Error(context.getString(R.string.empty_title_error))
        if(userInputSet.cast.isBlank())
            return EditState.Error(context.getString(R.string.cost_input_error))

        // another field can by Blank

        return EditState.Normal
    }

    private fun fillDb(userInputSet: UserInputSet){

        viewModelScope.launch {
            _state.value = EditState.Waiting
            delay(2000)
            _state.value = EditState.Finish
        }


    }

    private fun addPurchase(purchase: Purchase){
        viewModelScope.launch {
            val purchasesDao = Dependencies.getPurchasesDao()
            purchasesDao.insert(purchase)
            _purchases.value = purchasesDao.getAllAsTuple()
        }
    }



    private fun addShop(shop: Shop){
        viewModelScope.launch {
            val shopsDao = Dependencies.getShopsDao()
            shopsDao.insert(shop)
            _shops.value = shopsDao.getAllShops()
        }
    }

    private fun addTitle(title: Title){
        viewModelScope.launch {
            val titlesDao = Dependencies.getTitlesDao()
            titlesDao.inset(title)
            _titles.value = titlesDao.getAllTitles()
        }
    }

    private fun addProduct(product: Product){
        viewModelScope.launch {
            val productsDao = Dependencies.getProductsDao()
            productsDao.insert(product)
            _products.value = productsDao.getAllProducts()
        }
    }


}