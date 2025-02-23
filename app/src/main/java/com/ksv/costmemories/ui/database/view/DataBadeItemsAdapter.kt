package com.ksv.costmemories.ui.database.view

import android.util.Log
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
    private val onItemApplyClick: (DbItem, String) -> Unit,
    private val onItemDeleteClick: (DbItem) -> Unit
): ListAdapter<DbItem, DataBadeItemsAdapter.DBItemViewHolder>(DbItemDiffUtilCallback()) {

    class DBItemViewHolder(val binding: DbItemViewBinding): ViewHolder(binding.root){
        fun bind(item: DbItem,
                onItemApplyClick: (DbItem, String) -> Unit,
                onItemDeleteClick: (DbItem) -> Unit)
        {
            binding.itemEdit.setText(item.text)
            val counterTxt = "[${item.counter}]"
            binding.counter.text = counterTxt
            binding.delete.setOnClickListener { onItemDeleteClick(item) }
            binding.apply.setOnClickListener {
                //binding.itemEdit.clearFocus()
                onItemApplyClick(item, binding.itemEdit.text.toString().trim()) }
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
        holder.bind(currentList[position], onItemApplyClick, onItemDeleteClick)
    }
}