package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentMainBinding
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editPurchaseButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_editPurchaseFragment)
        }

        binding.fillDb.setOnClickListener {
            fillShops()
            fillTitles()
            fillProducts()
        }


    }
    private fun fillShops() {
        val shops = listOf(
            Shop(0, "Магнит"),
            Shop(0, "Пиволюбов"),
            Shop(0, "33 Курицы"),
            Shop(0, "Ермолино")
        )

        val shopsDao = Dependencies.getShopsDao()
        lifecycleScope.launch {
            shops.forEach {
                shopsDao.insert(it)
            }
        }
    }

    private fun fillTitles() {
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
        lifecycleScope.launch {
            titles.forEach { titleDao.inset(it) }
        }
    }

    private fun fillProducts() {
        val products = listOf(
            Product(name = "Сыр"),
            Product(name = "Пиво"),
            Product(name = "Мясо"),
            Product(name = "Колбаса"),
            Product(name = "Рыба"),
            Product(name = "Грибы")
        )
        val productsDao = Dependencies.getProductsDao()
        lifecycleScope.launch {
            products.forEach { productsDao.insert(it) }
//            groups.forEach { groupsDao.insertUnique(it.name) }
        }
    }
}