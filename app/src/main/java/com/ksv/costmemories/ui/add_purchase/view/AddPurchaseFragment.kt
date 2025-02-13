package com.ksv.costmemories.ui.add_purchase.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentAddPurchaseBinding
import com.ksv.costmemories.ui.add_purchase.model.AddPurchaseViewModel
import com.ksv.costmemories.ui.add_purchase.model.AddPurchaseViewModelFactory
import com.ksv.costmemories.ui.add_purchase.model.AddState
import com.ksv.costmemories.util.DateUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar

class AddPurchaseFragment : Fragment() {
    private var _binding: FragmentAddPurchaseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPurchaseViewModel by viewModels{
        AddPurchaseViewModelFactory(Dependencies.getPurchasesDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPurchaseBinding.inflate(layoutInflater)
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

        fillAutoCompleteData()

        binding.date.setOnClickListener { onDateClickListener() }
        binding.addButton.setOnClickListener { viewModel.onAddClick() }

        viewModel.state.onEach { state ->
            when (state) {
                AddState.Finish -> {
                    findNavController().navigate(R.id.action_addPurchaseFragment_to_homeFragment)
                    viewModel.onHomeFragmentNavigate()
                }
                is AddState.Error -> {
//                    Toast.makeText(requireContext(), getString(R.string.date_input_error), Toast.LENGTH_SHORT).show()
                    Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun fillAutoCompleteData() {
        viewModel.shops.onEach { shops ->
            val shopsToList = shops.map { it.shop_name }
            binding.shop.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    shopsToList
                )
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.titles.onEach { titles ->
            val titlesToList = titles.map { it.title }
            binding.title.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    titlesToList
                )
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.products.onEach { products ->
            val productsToList = products.map { it.group }
            binding.product.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    productsToList
                )
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun onDateClickListener() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getText(R.string.date_picker_title))
            .setSelection(Calendar.getInstance().timeInMillis)
            .build()

        datePicker.addOnPositiveButtonClickListener { timeInMills ->
            val dateString = DateUtils.dateFromMillsToString(timeInMills)
            binding.date.setText(dateString)
        }

        datePicker.show(childFragmentManager, datePicker::class.java.name)
    }


}
