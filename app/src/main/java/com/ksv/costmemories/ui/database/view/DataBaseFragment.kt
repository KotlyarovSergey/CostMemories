package com.ksv.costmemories.ui.database.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ksv.costmemories.Dependencies
import com.ksv.costmemories.R
import com.ksv.costmemories.databinding.FragmentDataBaseBinding
import com.ksv.costmemories.ui.database.entity.DbItem
import com.ksv.costmemories.ui.database.entity.DbItemType
import com.ksv.costmemories.ui.database.model.DataBaseViewModel
import com.ksv.costmemories.ui.database.model.DataBaseViewModelAdapter
import com.ksv.costmemories.ui.database.model.DbFragmentState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DataBaseFragment : Fragment() {
    private var _binding: FragmentDataBaseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DataBaseViewModel by viewModels {
        DataBaseViewModelAdapter(
            requireActivity().application,
            Dependencies.getPurchasesDao()
        )
    }

    private val adapter = DataBadeItemsAdapter(
        onApplyClick = { id, text ->  },
        onDeleteClick = { id ->  },
        onItemApplyClick = { item, text -> onItemApplyClick(item, text) },
        onItemDeleteClick = { item -> onItemDeleteClick(item)}
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_shops -> {
                    viewModel.onCheckedChange(DbItemType.SHOP)
                }

                R.id.radio_products -> {
                    viewModel.onCheckedChange(DbItemType.PRODUCT)
                }

                R.id.radio_titles -> {
                    viewModel.onCheckedChange(DbItemType.TITLE)
                }

                else -> {
                    throw IllegalArgumentException("Unknown radio button Id")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(200)
            when (viewModel.checkedRadio) {
                DbItemType.PRODUCT -> binding.radioProducts.isChecked = true
                DbItemType.TITLE -> binding.radioTitles.isChecked = true
                DbItemType.SHOP -> binding.radioShops.isChecked = true
            }
        }

        viewModel.items.onEach { items ->
            adapter.submitList(items)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.state.onEach { state ->
            when(state){
                DbFragmentState.Normal -> { }
                is DbFragmentState.DeleteConfirmRequest -> showDeleteRequest(state.id, state.request)
                is DbFragmentState.ReplaceConfirmRequest -> showMergeRequest(state.item, state.text, state.msg)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun showDeleteRequest(id: Long, msg: String){
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_db_item_delete_title)
            .setMessage(msg)
            .setPositiveButton(R.string.dialog_yes){ _,_ -> viewModel.onItemDeleteConfirm(id)}
            .setNegativeButton(R.string.dialog_no){_,_ -> }
            //.setOnDismissListener { viewModel.onConfirmDialogShow() }
            .show()

        viewModel.onConfirmDialogShow()
    }

    private fun showMergeRequest(item: DbItem, text: String, msg: String){
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_db_item_delete_title)
            .setMessage(msg)
            .setPositiveButton(R.string.dialog_yes){ _,_ -> viewModel.onItemReplaceConfirm(item, text)}
            .setNegativeButton(R.string.dialog_no){_,_ -> viewModel.onItemMergeDeny(item, text)}
            //.setOnDismissListener { viewModel.onConfirmDialogShow() }
            .show()

        viewModel.onConfirmDialogShow()
    }

    private fun onApplyClick(id: Long, text: String) {
        Toast.makeText(requireContext(), "apply on id: $id", Toast.LENGTH_SHORT).show()
    }

    private fun onDeleteClick(id: Long) {
        Toast.makeText(requireContext(), "delete on id: $id", Toast.LENGTH_SHORT).show()
    }

    private fun onItemApplyClick(item: DbItem, text: String) {
//        Toast.makeText(requireContext(), "apply on id: ${item.text}, ${item.type}", Toast.LENGTH_SHORT).show()
        viewModel.onItemApplyClick(item, text)
    }

    private fun onItemDeleteClick(item: DbItem) {
        //Toast.makeText(requireContext(), "delete on id: ${item.text}", Toast.LENGTH_SHORT).show()
        viewModel.onItemDeleteClick(item)
    }
}