package com.ksv.costmemories.ui.edit_purchase.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentEditPurchaseBinding
import com.ksv.costmemories.ui.edit_purchase.model.EditPurchaseViewModel
import com.ksv.costmemories.ui.edit_purchase.model.EditPurchaseViewModelFactory
import com.ksv.costmemories.ui.edit_purchase.model.EditState
import com.ksv.costmemories.util.DateUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar

class EditPurchaseFragment : Fragment() {
    private var _binding: FragmentEditPurchaseBinding? = null
    private val binding get() = _binding!!
    private val viewModel : EditPurchaseViewModel by viewModels{
        EditPurchaseViewModelFactory(
            Dependencies.getPurchasesDao(),
            EditPurchaseFragmentArgs.fromBundle(requireArguments()).id
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPurchaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        fillAutoCompleteData()

        viewModel.purchaseTuple.onEach { purchase ->
            //Log.d("ksvlog", "purchase: $purchase")
            purchase?.let {
                binding.date.setText(purchase.date)
                binding.product.setText(purchase.product)
                binding.title.setText(purchase.title)
                binding.shop.setText(purchase.shop)
                val cost = if(purchase.cost == 0) "" else purchase.cost.toString()
                binding.cost.setText(cost)
                binding.comment.setText(purchase.comment)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.state.onEach { state->
            when (state){
                EditState.SetDate -> {
                    openDateDialog()
                    viewModel.onDateDialogOpen()
                }
                EditState.Finish -> {
                    findNavController().navigate(R.id.action_editPurchaseFragment_to_homeFragment)
                    viewModel.onHomeNavigate()
                }
                EditState.Delete -> {
                    openDeleteConfirmDialog()
                }
                is EditState.Error -> {
                    Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                    viewModel.onErrorMsgShow()
                }
                else -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun fillAutoCompleteData() {
        viewModel.shops.onEach { shops ->
            val shopsToList = shops.map { it.shop }
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
            val productsToList = products.map { it.product }
            binding.product.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    productsToList
                )
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun openDateDialog() {
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

    private fun openDeleteConfirmDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_purchase_delete_title)
            .setMessage(R.string.dialog_purchase_delete_message)
            .setPositiveButton(R.string.dialog_yes){ _, _ ->
                viewModel.onDeleteConfirm()
            }
            .setNegativeButton(R.string.dialog_no) {_, _ -> }
            .setOnDismissListener {
                viewModel.onDeleteConfirmDialogDismiss()
            }
            .show()
    }
}