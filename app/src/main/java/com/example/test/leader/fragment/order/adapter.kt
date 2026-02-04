package com.example.test.leader.fragment.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.data.Order
import com.example.test.data.OrderStatus
import com.example.test.databinding.LeaderActivityAdapterBinding
class OrderAdapter(
    private val onItemClick: (Order) -> Unit,
    private val onAcceptClick: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.VH>(DIFF) {

    class VH(val vb: LeaderActivityAdapterBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vb = LeaderActivityAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(vb)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.vb.apply {
            txtTitle.text = item.title
            txtMeta.text = "${item.startTime}  ·  ${item.from} → ${item.to}"
            txtPrice.text = "${item.price}  ·  ${item.peopleCount}人"
            txtTags.text = item.tags.joinToString(" · ")

            val canAccept = item.status == OrderStatus.AVAILABLE
            btnAccept.isEnabled = canAccept
            btnAccept.text = when (item.status) {
                OrderStatus.AVAILABLE -> "接单"
                OrderStatus.ACCEPTED_BY_ME -> "已接（我）"
                OrderStatus.TAKEN_BY_OTHER -> "已被接走"
                OrderStatus.EXPIRED -> "已过期"
            }

            root.setOnClickListener { onItemClick(item) }
            btnAccept.setOnClickListener { onAcceptClick(item) }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
        }
    }
}