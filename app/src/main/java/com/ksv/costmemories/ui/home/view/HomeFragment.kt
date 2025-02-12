package com.ksv.costmemories.ui.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentHomeBinding
import com.ksv.costmemories.ui.home.model.HomeViewModel
import com.ksv.costmemories.ui.home.model.HomeViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addEditFragment)
        }

//        binding.fillDb.setOnClickListener {
//            lifecycleScope.launch {
//                FillDb.fill()
//            }
//        }


        //viewModel.setFilter("саль")
        viewModel.purchases.onEach { purchasesList ->
//            Log.d("ksvlog", purchasesList.toString())
            adapter.submitList(purchasesList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun onItemClick(id: Long){

    }

    private fun onItemLongClick(id: Long): Boolean{

        return true
    }
}