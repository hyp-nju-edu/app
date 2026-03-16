package com.example.test.leader.fragment.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import androidx.navigation.fragment.findNavController
import com.example.test.data.FakeRepository
import com.example.test.data.OrderStatus
import com.example.test.databinding.LeaderFragmentOrdersBinding

class OrderListFragment : Fragment(R.layout.leader_fragment_orders) {

    private var _vb: LeaderFragmentOrdersBinding? = null
    private val vb get() = _vb!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _vb = LeaderFragmentOrdersBinding.bind(view)

        val adapter = OrderAdapter(
            onItemClick = { order ->
                // 点击卡片跳转到接单确认页面（只对可接单的订单）
                if (order.status == OrderStatus.AVAILABLE) {
                    val args = Bundle().apply { putString("orderId", order.id) }
                    findNavController().navigate(R.id.action_orders_to_orderConfirm, args)
                } else {
                    // 对其他状态的原先的详情页逻辑（可选）
                    val args = Bundle().apply { putString("orderId", order.id) }
                    findNavController().navigate(R.id.action_orders_to_orderDetail, args)
                }
            },
            onAcceptClick = { order ->
                // 现在接单按钮已经变成整个卡片，这个回调实际上不会单独被调用
                // 保留以防万一，可以不做操作或者提示用户点击卡片
            }
        )

        vb.recycler.layoutManager = LinearLayoutManager(context)
        vb.recycler.adapter = adapter
        adapter.submitList(FakeRepository.listOrders())
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }
}