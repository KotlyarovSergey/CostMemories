package com.ksv.costmemories.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.data.Repository
import com.ksv.costmemories.databinding.FragmentSettingsBinding
import com.ksv.costmemories.entity.Shop
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exportDb.setOnClickListener {
            val repository = Repository()
            lifecycleScope.launch {
                repository.exportPurchasesTuplesToCsv(requireContext(), FILE_NAME)
            }
        }

        binding.importDb.setOnClickListener {
            lifecycleScope.launch {
                binding.progress.visibility = View.VISIBLE

                val repository = Repository()
                repository.importPurchasesTuplesFromCsv(requireContext(), FILE_NAME)

                //val purchases = repository.importPurchasesTuplesFromCsv(requireContext(), FILE_NAME)
                //val newText = binding.tvResult.text.toString() + "\n" + purchases.toString()
                //binding.tvResult.text = newText

                binding.progress.visibility = View.GONE
            }
        }

//        binding.importDb.setOnClickListener {
//            val purchasesDao = Dependencies.getPurchasesDao()
////            val shop = Shop(0, "Roosty")
//            val shop = Shop(0, "Магнит")
//            viewLifecycleOwner.lifecycleScope.launch {
//                val id = purchasesDao.getShopByName(shop.shop)
//                Log.d("ksvlog","id: $id")
//                Toast.makeText(requireContext(), "${shop.shop} add, id: $id", Toast.LENGTH_LONG).show()
//            }
//
//        }


    }

    companion object{
        private const val FILE_NAME = "purchases.csv"
    }
}