package com.ksv.costmemories.supporded

import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.entity.Group
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title

object FillDb {
    suspend fun fill() {
        fillShops()
        fillProducts()
        fillGroups()
    }

    private suspend fun fillShops() {
        val shops = listOf(
            Shop(shop = "Магнит"),
            Shop(shop = "Пиволюбов"),
            Shop(shop = "33 Курицы"),
            Shop(shop = "Ермолино")
        )

//        val shopsDao = Dependencies.getShopsDao()
//        shops.forEach { shopsDao.insert(it) }

        val purchasesDao = Dependencies.getPurchasesDao()
        shops.forEach { purchasesDao.shopInsert(it) }
    }

    private suspend fun fillProducts() {
        val titles = listOf(
            Title(title = "Российский"),
            Title(title = "Тильзиталь"),
            Title(title = "Ламбер"),
            Title(title = "Хадыженское"),
            Title(title = "Ческое"),
            Title(title = "СССР"),
            Title(title = "Филе"),
            Title(title = "Бедра"),
            Title(title = "Крылья"),
            Title(title = "Шея"),
            Title(title = "Окорок"),
            Title(title = "Финский"),
            Title(title = "Каневской"),
            Title(title = "Сормовская"),
            Title(title = "Селедка"),
            Title(title = "Карась половинка"),
            Title(title = "Путасу"),
            Title(title = "Тарань"),
            Title(title = "Вешенки маринованые"),
            Title(title = "Весенки сырые"),
            Title(title = "Шампиньоны сырые"),
            Title(title = "Ассорти")
        )
//        val titleDao = Dependencies.getTitlesDao()
//        titles.forEach { titleDao.inset(it) }

        val purchasesDao = Dependencies.getPurchasesDao()
        titles.forEach { purchasesDao.titleInsert(it) }
    }

    private suspend fun fillGroups() {
        val products = listOf(
            Group(product = "Сыр"),
            Group(product = "Пиво"),
            Group(product = "Мясо"),
            Group(product = "Колбаса"),
            Group(product = "Рыба"),
            Group(product = "Грибы")
        )
//        val productsDao = Dependencies.getProductsDao()
//        products.forEach { productsDao.insert(it) }

        val purchasesDao = Dependencies.getPurchasesDao()
        products.forEach { purchasesDao.productInsert(it) }
    }
}