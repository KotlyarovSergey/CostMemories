package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentMainBinding
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.supporded.FillDb
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel : DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Log.d("ksvlog", "MainFragment createdView")
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
            lifecycleScope.launch {
                FillDb.fill()
            }
        }

        dataViewModel.purchase.onEach { purchasesList ->
            purchasesListHasChange(purchasesList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun purchasesListHasChange(purchaseList: List<PurchaseTuple>){
//        val text = purchaseList.toString()
        val text = purchaseList.reversed().joinToString("\n")
        binding.textView.text = text
    }

}