package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentEditPurchaseBinding
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

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
        setDate()
        binding.dateButton.setOnClickListener { dateButtonOnClickListener() }
    }

    private fun setDate(){
        binding.dateEdit.setText(getCurrentDate())
    }

    private fun dateButtonOnClickListener() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getText(R.string.hello_blank_fragment))
            .build()

        datePicker.addOnPositiveButtonClickListener { timeInMills ->
            val dateString = dateFromMillsToString(timeInMills)
            binding.dateEdit.setText(dateString)
        }

        datePicker.show(childFragmentManager, datePicker::class.java.name)
    }

    private fun getCurrentDate(): String {
        return dateFromMillsToString(Calendar.getInstance().timeInMillis)
    }

    private fun dateFromMillsToString(mills: Long): String{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mills
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formattedText = LocalDate.of(year, month, day).format(formatter)
        return formattedText
    }


    private fun getShops() {
        val shopsDao = Dependencies.getShopsDao()
        lifecycleScope.launch {
            shops = shopsDao.getAllShops().toMutableList()
            fillShops()
        }
    }

    private fun getProducts() {
        val productsDao = Dependencies.getProductsDao()
        lifecycleScope.launch {
            products = productsDao.getAllProducts().toMutableList()
            fillProducts()
        }
    }

    private fun getTitles() {
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

    private fun fillShops() {
        val shopsToList = shops.map { it.shop_name }
        binding.shop.setAdapter(
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                shopsToList
            )
        )
    }

    private fun fillTitles() {
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