package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentEditPurchaseBinding
import com.ksv.costmemories.entity.Product
import com.ksv.costmemories.entity.Purchase
import com.ksv.costmemories.entity.Shop
import com.ksv.costmemories.entity.Title
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
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
        binding.addButton.setOnClickListener { addButtonOnClickListener() }
    }

    private fun addButtonOnClickListener() {
        val purchase = checkFieldsAndGetPurchase()
        if (purchase != null) {
            val purchasesDao = Dependencies.getPurchasesDao()
            lifecycleScope.launch {
                purchasesDao.insert(purchase)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.add_purchase_success),
                    Toast.LENGTH_SHORT
                    ).show()
            }
            parentFragmentManager.popBackStack()
        }

    }

    private fun checkFieldsAndGetPurchase(): Purchase? {
        val date = checkDate()
        if (date == null) {
            Toast.makeText(
                requireContext(),
                resources.getText(R.string.date_input_error),
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
        val shop = checkShop()
        val product = checkProduct()
        val title = checkTitle()
        if (title == null) {
            Toast.makeText(
                requireContext(),
                resources.getText(R.string.empty_title_error),
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
        val cost = checkCost()
        if (cost == null) {
            Toast.makeText(
                requireContext(),
                resources.getText(R.string.cost_input_error),
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
        val comment = checkComment()

        return Purchase(
            date = date,
            shopId = shop!!.id,
            cost = cost,
            comment = comment!!,
            productId = product!!.id,
            titleId = title.id
        )
    }

    private fun checkCost(): Int? {
        val inputText = binding.cast.text.toString().trim()
        return try {
            inputText.toInt()
        } catch (exception: Exception) {
            null
        }
    }

    private fun checkDate(): String? {
        val dateDimeText = binding.dateEdit.text.toString().trim()
        val parts = dateDimeText.split('.', '/')
        if(parts.size != 3)
            return null
        val day = parts[0]
        val month = parts[1]
        val year = parts[2]

        try {
            LocalDate.of(year.toInt(), month.toInt(), day.toInt())
            return dateDimeText
        }catch (exception: Exception) {
            return null
        }
//        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
//        try {
//            LocalDateTime.parse(dateDimeText, formatter)
//            return dateDimeText
//        } catch (exception: Exception) {
//            return null
//        }
    }

    private fun checkComment(): String? {
        return binding.comment.text.toString().trim()
    }

    private fun checkTitle(): Title? {
        val inputText = binding.title.text.toString().trim()
        if (inputText.isBlank())
            return null

        titles.forEach { title ->
            if (title.text == inputText)
                return title
        }

        val title = Title(text = inputText)
        val titleDao = Dependencies.getTitlesDao()
        lifecycleScope.launch {
            titleDao.inset(title)
            titles = titleDao.getAllTitles()
            fillTitleTextView()
        }
        return title
    }

    private fun checkProduct(): Product? {
        val inputText = binding.product.text.toString().trim()
        if (inputText.isBlank())
            return null

        products.forEach { product ->
            if (product.name == inputText)
                return product
        }

        val product = Product(name = inputText)
        val productsDao = Dependencies.getProductsDao()
        lifecycleScope.launch {
            productsDao.insert(product)
            products = productsDao.getAllProducts()
            fillProductTextView()
        }
        return product
    }

    private fun checkShop(): Shop? {
        val inputText = binding.shop.text.toString().trim()
        if (inputText.isBlank())
            return null

        shops.forEach {
            if (inputText == it.shop_name) {
                return it
            }
        }

        val shop = Shop(shop_name = inputText)
        val shopsDao = Dependencies.getShopsDao()
        lifecycleScope.launch {
            shopsDao.insert(shop)
            shops = shopsDao.getAllShops()
            fillShopTextView()
        }

        return shop
    }

    private fun setDate() {
        binding.dateEdit.setText(getCurrentDate())
    }

    private fun dateButtonOnClickListener() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getText(R.string.date_picker_title))
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

    private fun dateFromMillsToString(mills: Long): String {
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
            fillShopTextView()
        }
    }

    private fun getProducts() {
        val productsDao = Dependencies.getProductsDao()
        lifecycleScope.launch {
            products = productsDao.getAllProducts().toMutableList()
            fillProductTextView()
        }
    }

    private fun getTitles() {
        val titlesDao = Dependencies.getTitlesDao()
        lifecycleScope.launch {
            titles = titlesDao.getAllTitles().toMutableList()
            fillTitleTextView()
        }
    }

    private fun fillProductTextView() {
        val productToList = products.map { it.name }
        binding.product.setAdapter(
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                productToList
            )
        )
    }

    private fun fillShopTextView() {
        val shopsToList = shops.map { it.shop_name }
        binding.shop.setAdapter(
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                shopsToList
            )
        )
    }

    private fun fillTitleTextView() {
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