package com.ksv.costmemories.ui.database.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ksv.costmemories.databinding.DbItemViewBinding
import com.ksv.costmemories.ui.database.entity.DbItem

class DataBadeItemsAdapter(
    private val onApplyClick: (Long, String) -> Unit,
    private val onDeleteClick: (Long) -> Unit
): ListAdapter<DbItem, DataBadeItemsAdapter.DBItemViewHolder>(DbItemDiffUtilCallback()) {

    class DBItemViewHolder(val binding: DbItemViewBinding): ViewHolder(binding.root){
        fun bind(item: DbItem, onApplyClick: (Long, String) -> Unit, onDeleteClick: (Long) -> Unit){
            binding.itemEdit.setText(item.text)
            binding.itemEdit.doOnTextChanged { text, _, _, _ ->
                val newText = text.toString().trim()
                binding.apply.visibility =
                    if (item.text.compareTo(newText, true) == 0)
                        View.INVISIBLE
                    else
                        View.VISIBLE
            }
            val counterTxt = "[${item.counter}]"
            binding.counter.text = counterTxt
            binding.delete.setOnClickListener { onApplyClick(item.id, binding.itemEdit.text.toString().trim()) }
            binding.apply.setOnClickListener { onDeleteClick(item.id) }
        }
    }

    class DbItemDiffUtilCallback: DiffUtil.ItemCallback<DbItem>() {
        override fun areItemsTheSame(oldItem: DbItem, newItem: DbItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DbItem, newItem: DbItem): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBItemViewHolder {
        return DBItemViewHolder(
            DbItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DBItemViewHolder, position: Int) {
        holder.bind(currentList[position], onApplyClick, onDeleteClick)
    }
}