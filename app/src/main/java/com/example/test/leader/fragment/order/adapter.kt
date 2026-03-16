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
            // 显示客户姓名
            txtCustomerName.text = "客户：${item.customerName}"

            // 显示路线名称
            txtTitle.text = item.title

            // 显示起点和终点
            txtRoute.text = "${item.from} → ${item.to}"

            // 显示途经景点（tags）
            txtTags.text = "途经：${item.tags.joinToString(" · ")}"

            // 显示时间
            txtTime.text = item.startTime

            // 显示价格和人数
            txtPrice.text = "${item.price} · ${item.peopleCount}人"

            // 根据订单状态设置卡片样式
            val canAccept = item.status == OrderStatus.AVAILABLE

            // 设置卡片背景色和点击状态
            // 使用白色作为基础，通过改变文字颜色或卡片背景来表示状态
            when (item.status) {
                OrderStatus.AVAILABLE -> {
                    // 可接单：绿色边框和浅绿背景提示
                    root.strokeWidth = 3
                    root.setCardBackgroundColor(0xFFE8F5E9.toInt()) // 浅绿色
                    root.isClickable = true
                    root.isEnabled = true
                }
                OrderStatus.ACCEPTED_BY_ME -> {
                    // 我已接单：蓝色边框和浅蓝背景
                    root.strokeWidth = 3
                    root.setCardBackgroundColor(0xFFE3F2FD.toInt()) // 浅蓝色
                    root.isClickable = false
                    root.isEnabled = false
                }
                OrderStatus.TAKEN_BY_OTHER -> {
                    // 已被接走：灰色
                    root.strokeWidth = 0
                    root.setCardBackgroundColor(0xFFF5F5F5.toInt()) // 浅灰色
                    root.isClickable = false
                    root.isEnabled = false
                }
                OrderStatus.EXPIRED -> {
                    // 已过期：浅红色
                    root.strokeWidth = 0
                    root.setCardBackgroundColor(0xFFFFEBEE.toInt()) // 浅红色
                    root.isClickable = false
                    root.isEnabled = false
                }
            }

            // 设置卡片点击事件
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
        }
    }
}