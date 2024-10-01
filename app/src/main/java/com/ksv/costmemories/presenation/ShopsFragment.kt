package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ksv.costmemories.App
import com.ksv.costmemories.R
import com.ksv.costmemories.data.ShopDao
import com.ksv.costmemories.databinding.FragmentShopsBinding
import com.ksv.costmemories.entity.Shop
import kotlinx.coroutines.launch

class ShopsFragment : Fragment() {
    private lateinit var shopDao: ShopDao
    private var _binding: FragmentShopsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopDao = (activity?.application as App).db.shopDao()

        binding.addButton.setOnClickListener {
            val shopName = binding.editText.text.toString()
            val shop = Shop(name = shopName)
            lifecycleScope.launch {
                shopDao.insert(shop)
                refresh()
            }
        }
        binding.refreshButton.setOnClickListener {
            refresh()
        }
        binding.delButton.setOnClickListener {
            val number = binding.numberEdit.text.toString().toInt()
            lifecycleScope.launch {
                val shops = shopDao.getAll()
                shops.forEach {
                    if(it.id == number){
                        shopDao.delete(it)
                    }
                }
                refresh()
            }
        }
        binding.updButton.setOnClickListener {
            val number = binding.numberEdit.text.toString().toInt()
            lifecycleScope.launch {
                val shops = shopDao.getAll()
                shops.forEach {
                    if(it.id == number){
                        val shopNewName = binding.editText.text.toString()
                        val newShop = Shop(it.id, shopNewName)
                        shopDao.update(newShop)
                        refresh()
                    }
                }
            }
        }
    }

    private fun refresh(){
        lifecycleScope.launch {
            val shops = shopDao.getAll()
            val text = shops.joinToString(separator = "\n") { it.toString() }
            binding.tvInfo.text = text
        }
    }
}