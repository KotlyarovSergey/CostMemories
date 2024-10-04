package com.ksv.costmemories.supporded

import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title

object FillDb {
    suspend fun fill() {
        fillShops()
        fillTitles()
        fillProducts()
    }

    private suspend fun fillShops() {
        val shops = listOf(
            Shop(0, "Магнит"),
            Shop(0, "Пиволюбов"),
            Shop(0, "33 Курицы"),
            Shop(0, "Ермолино")
        )

        val shopsDao = Dependencies.getShopsDao()
        shops.forEach {
            shopsDao.insert(it)
        }

    }

    private suspend fun fillTitles() {
        val titles = listOf(
            Title(text = "Российский"),
            Title(text = "Тильзиталь"),
            Title(text = "Ламбер"),
            Title(text = "Хадыженское"),
            Title(text = "Ческое"),
            Title(text = "СССР"),
            Title(text = "Филе"),
            Title(text = "Бедра"),
            Title(text = "Крылья"),
            Title(text = "Шея"),
            Title(text = "Окорок"),
            Title(text = "Финский"),
            Title(text = "Каневской"),
            Title(text = "Сормовская"),
            Title(text = "Селедка"),
            Title(text = "Карась половинка"),
            Title(text = "Путасу"),
            Title(text = "Тарань"),
            Title(text = "Вешенки маринованые"),
            Title(text = "Весенки сырые"),
            Title(text = "Шампиньоны сырые"),
            Title(text = "Ассорти")
        )
        val titleDao = Dependencies.getTitlesDao()
        titles.forEach { titleDao.inset(it) }
    }

    private suspend fun fillProducts() {
        val products = listOf(
            Product(name = "Сыр"),
            Product(name = "Пиво"),
            Product(name = "Мясо"),
            Product(name = "Колбаса"),
            Product(name = "Рыба"),
            Product(name = "Грибы")
        )
        val productsDao = Dependencies.getProductsDao()
        products.forEach { productsDao.insert(it) }
    }
}