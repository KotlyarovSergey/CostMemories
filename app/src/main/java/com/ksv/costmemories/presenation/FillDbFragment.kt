package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ksv.costmemories.App
import com.ksv.costmemories.databinding.FragmentFillDbBinding
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.Shop
import kotlinx.coroutines.launch

class FillDbFragment : Fragment() {
    private var _binding: FragmentFillDbBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFillDbBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fillDbButton.setOnClickListener {
            addShops()
            addProducts()
            addPurchases()
        }

        binding.showButton.setOnClickListener {
            with(binding) {
                if (radioShops.isChecked) {
                    showShops()
                } else if (radioProducts.isChecked) {
                    showProducts()
                } else if (radioPurchases.isChecked) {
                    showPurchases()
                }

            }
        }
    }

    private fun addShops() {
        val shops = listOf(
            Shop(name = "Магнит"),
            Shop(name = "33 Курицы"),
            Shop(name = "Пиволюбов"),
            Shop(name = "Пятёрочка")
        )
        val shopDao = (activity?.application as App).db.shopDao()
        lifecycleScope.launch {
            shops.forEach { shopDao.insert(it) }
        }
    }

    private fun showShops() {
        val shopDao = (activity?.application as App).db.shopDao()
        lifecycleScope.launch {
            val shops = shopDao.getAll()
            binding.textView.text = shops.toString()
        }
    }

    private fun addProducts() {
        val products = listOf(
            Product(name = "Молоко"),
            Product(name = "Бёдра"),
            Product(name = "Грудка"),
            Product(name = "Кефир"),
            Product(name = "Сахар"),
        )

        val productDao = (activity?.application as App).db.productDao()
        lifecycleScope.launch {
            products.forEach {
                productDao.insert(it)
            }
        }

    }

    private fun showProducts() {
        val productDao = (activity?.application as App).db.productDao()
        lifecycleScope.launch {
            val products = productDao.getAll()
            val text = products.joinToString(separator = "\n") { "$it" }
            binding.textView.text = text
        }
    }

    private fun addPurchases() {
        val purchaseDao = (activity?.application as App).db.purchaseDao()
        val purchases = listOf(
            Purchase(
                date = "15.08.24",
                productId = 1,
                shopId = 1,
                cost = 69
            ),
            Purchase(
                date = "21.08.24",
                productId = 4,
                shopId = 1,
                cost = 69
            ),
            Purchase(
                date = "22.08.24",
                productId = 2,
                shopId = 2,
                cost = 457
            )
        )
        lifecycleScope.launch {
            purchases.forEach {
                purchaseDao.insert(it)
            }

        }
    }

    private fun showPurchases() {
        val purchaseDao = (activity?.application as App).db.purchaseDao()
        lifecycleScope.launch {
            val purchases = purchaseDao.getAll()
//            val text = products.joinToString(separator = "\n") { "$it" }
//            binding.textView.text = text
            binding.textView.text = purchases.toString()
        }
    }


}