package com.ksv.costmemories.presenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentEditPurchaseBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class EditPurchaseFragment : Fragment() {
    private var _binding: FragmentEditPurchaseBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel: MainViewModel by activityViewModels()

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
        setDataHasChangeListeners()

        setDate()
        binding.dateEdit.setOnClickListener { dateEditOnClickListener() }
        binding.addButton.setOnClickListener { addButtonOnClickListener() }
    }

    private fun setDataHasChangeListeners() {
        dataViewModel.shops.onEach { shops ->
            val shopsToList = shops.map { it.shop_name }
            binding.shop.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    shopsToList
                )
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        dataViewModel.titles.onEach { titles ->
            val titlesToList = titles.map { it.title }
            binding.title.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    titlesToList
                )
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        dataViewModel.products.onEach { products ->
            val productsToList = products.map { it.group }
            binding.product.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    productsToList
                )
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        dataViewModel.state.onEach { state ->
            binding.progress.visibility = View.INVISIBLE
            when (state) {
                is EditState.Error -> {
                    //Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                EditState.Finish -> {
                    //findNavController().popBackStack()
                }

                EditState.Normal -> {
                    binding.progress.visibility = View.INVISIBLE
                }

                EditState.Waiting -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun addButtonOnClickListener() {
        val userInputSet = with(binding) {
            UserInputSet(
                date = dateEdit.text.toString().trim(),
                product = product.text.toString().trim(),
                title = title.text.toString().trim(),
                shop = shop.text.toString().trim(),
                cost = cost.text.toString().trim(),
                comment = comment.text.toString().trim()
            )
        }
        val checkResult = dataViewModel.checkUserInput(userInputSet)
        when(checkResult){
            InputCheckResult.OK -> {
                dataViewModel.fillDb(userInputSet)
                findNavController().popBackStack()
            }
            InputCheckResult.EMPTY_DATE -> {
                Toast.makeText(requireContext(), getString(R.string.date_input_error), Toast.LENGTH_SHORT).show()
            }
            InputCheckResult.EMPTY_COST -> {
                Toast.makeText(requireContext(), getString(R.string.cost_input_error), Toast.LENGTH_SHORT).show()
            }
            InputCheckResult.EMPTY_TITLE -> {
                Toast.makeText(requireContext(), getString(R.string.empty_title_error), Toast.LENGTH_SHORT).show()
            }
            InputCheckResult.UNKNOWN -> { }
        }
    }

    private fun setDate() {
        binding.dateEdit.setText(getCurrentDate())
    }

    private fun dateEditOnClickListener() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getText(R.string.date_picker_title))
            .setSelection(Calendar.getInstance().timeInMillis)
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
}
