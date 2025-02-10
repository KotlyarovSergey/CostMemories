package com.ksv.costmemories.supporded

import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.entity.Group
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Product

object FillDb {
    suspend fun fill() {
        fillShops()
        fillProducts()
        fillGroups()
    }

    private suspend fun fillShops() {
        val shops = listOf(
            Shop(shop_name = "Магнит"),
            Shop(shop_name = "Пиволюбов"),
            Shop(shop_name = "33 Курицы"),
            Shop(shop_name = "Ермолино")
        )

        val shopsDao = Dependencies.getShopsDao()
        shops.forEach {
            shopsDao.insert(it)
        }

    }

    private suspend fun fillProducts() {
        val titles = listOf(
            Product(title = "Российский"),
            Product(title = "Тильзиталь"),
            Product(title = "Ламбер"),
            Product(title = "Хадыженское"),
            Product(title = "Ческое"),
            Product(title = "СССР"),
            Product(title = "Филе"),
            Product(title = "Бедра"),
            Product(title = "Крылья"),
            Product(title = "Шея"),
            Product(title = "Окорок"),
            Product(title = "Финский"),
            Product(title = "Каневской"),
            Product(title = "Сормовская"),
            Product(title = "Селедка"),
            Product(title = "Карась половинка"),
            Product(title = "Путасу"),
            Product(title = "Тарань"),
            Product(title = "Вешенки маринованые"),
            Product(title = "Весенки сырые"),
            Product(title = "Шампиньоны сырые"),
            Product(title = "Ассорти")
        )
        val titleDao = Dependencies.getTitlesDao()
        titles.forEach { titleDao.inset(it) }
    }

    private suspend fun fillGroups() {
        val products = listOf(
            Group(group = "Сыр"),
            Group(group = "Пиво"),
            Group(group = "Мясо"),
            Group(group = "Колбаса"),
            Group(group = "Рыба"),
            Group(group = "Грибы")
        )
        val productsDao = Dependencies.getProductsDao()
        products.forEach { productsDao.insert(it) }
    }
}