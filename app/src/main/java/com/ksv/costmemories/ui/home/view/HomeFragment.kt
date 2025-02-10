package com.ksv.costmemories.ui.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentHomeBinding
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.presenation.MainViewModel
import com.ksv.costmemories.supporded.FillDb
import com.ksv.costmemories.ui.home.model.HomeViewModel
import com.ksv.costmemories.ui.home.model.HomeViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    //private val dataViewModel : MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(Dependencies.getPurchasesDao())
    }
    private val adapter = PurchaseAdapter(
        onClick = { onItemClick(it) },
        onLongClick = { onItemLongClick(it) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Log.d("ksvlog", "MainFragment createdView")
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        binding.editPurchaseButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_editPurchaseFragment)
        }

        binding.fillDb.setOnClickListener {
            lifecycleScope.launch {
                FillDb.fill()
            }
        }

//        dataViewModel.purchase.onEach { purchasesList ->
//            purchasesListHasChange(purchasesList)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)


        //viewModel.setFilter("саль")
        viewModel.purchases.onEach { purchasesList ->
            //purchasesListHasChange(purchasesList)
            Log.d("ksvlog", purchasesList.toString())
            adapter.submitList(purchasesList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun purchasesListHasChange(purchaseList: List<PurchaseTuple>){
//        val text = purchaseList.toString()
        val text = purchaseList.reversed().joinToString("\n")
        //binding.textView.text = text
    }

    private fun onItemClick(id: Long){

    }

    private fun onItemLongClick(id: Long): Boolean{

        return true
    }
}