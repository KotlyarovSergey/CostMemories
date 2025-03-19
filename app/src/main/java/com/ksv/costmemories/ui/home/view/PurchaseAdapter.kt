package com.ksv.costmemories.ui.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ksv.costmemories.databinding.PurchaseViewBinding
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.util.DateUtils

class PurchaseAdapter(
    private val onClick: (Long) -> Unit,
    private val onLongClick: (Long) -> Boolean
): ListAdapter<PurchaseTuple, PurchaseAdapter.PurchaseViewHolder>(DiffUtilCallback()) {

    inner class PurchaseViewHolder(private val binding: PurchaseViewBinding):
        ViewHolder(binding.root){
        fun bind(purchase: PurchaseTuple, onClick: (Long) -> Unit, onLongClick: (Long) -> Boolean){
            binding.purchase = purchase
            binding.date.text = DateUtils.millsToShortDateFormat(purchase.milliseconds)
             binding.rootView.setOnClickListener { onClick(purchase.id) }
            binding.rootView.setOnLongClickListener { onLongClick(purchase.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        return PurchaseViewHolder(
            PurchaseViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val purchase = currentList[position]
        holder.bind(purchase, onClick, onLongClick)
    }

    class DiffUtilCallback: DiffUtil.ItemCallback<PurchaseTuple>(){
        override fun areItemsTheSame(oldItem: PurchaseTuple, newItem: PurchaseTuple) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PurchaseTuple, newItem: PurchaseTuple) =
            oldItem == newItem

    }

}