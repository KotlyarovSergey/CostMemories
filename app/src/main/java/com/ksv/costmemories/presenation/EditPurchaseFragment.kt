package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.databinding.FragmentEditPurchaseBinding
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title
import kotlinx.coroutines.launch

class EditPurchaseFragment : Fragment() {
    private var _binding: FragmentEditPurchaseBinding? = null
    private val binding get() = _binding!!

    private var shops: List<Shop> = emptyList()
    private var products: List<Product> = emptyList()
    private var titles: List<Title> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPurchaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getShops()
        getProducts()
        getTitles()
    }

    private fun getShops(){
        val shopsDao = Dependencies.getShopsDao()
        lifecycleScope.launch {
            shops = shopsDao.getAllShops().toMutableList()
            fillShops()
        }
    }

    private fun getProducts(){
        val productsDao = Dependencies.getProductsDao()
        lifecycleScope.launch {
            products = productsDao.getAllProducts().toMutableList()
            fillProducts()
        }
    }

    private fun getTitles(){
        val titlesDao = Dependencies.getTitlesDao()
        lifecycleScope.launch {
            titles = titlesDao.getAllProducts().toMutableList()
            fillTitles()
        }
    }

    private fun fillProducts() {
        val productToList = products.map { it.name }
        binding.product.setAdapter(
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                productToList
                )
        )
    }

    private fun fillShops(){
        val shopsToList = shops.map { it.shop_name }
        binding.shop.setAdapter(ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, shopsToList))
    }

    private fun fillTitles(){
        val titleToList = titles.map { it.text }
        binding.title.setAdapter(
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                titleToList
            )
        )
    }
}