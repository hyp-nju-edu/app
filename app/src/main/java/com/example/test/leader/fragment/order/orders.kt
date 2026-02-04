package com.example.test.leader.fragment.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.test.R
import androidx.navigation.fragment.findNavController
import com.example.test.data.FakeRepository
import com.example.test.databinding.LeaderFragmentOrdersBinding

class OrderListFragment : Fragment(R.layout.leader_fragment_orders) {

    private var _vb: LeaderFragmentOrdersBinding? = null
    private val vb get() = _vb!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _vb = LeaderFragmentOrdersBinding.bind(view)

        val adapter = OrderAdapter(
            onItemClick = { order ->
                val args = Bundle().apply { putString("orderId", order.id) }
                findNavController().navigate(R.id.action_orders_to_orderDetail, args)
            },
            onAcceptClick = { order ->
                val result = FakeRepository.acceptOrder(order.id)
                // 简化：接单后刷新列表

                // 接单成功则跳聊天
                if (result.isSuccess) {
                    val conv = FakeRepository.getConversationByOrder(order.id)
                    if (conv != null) {
                        val args = Bundle().apply {
                            putString("conversationId", conv.id)
                            putString("orderId", order.id)
                        }
                        findNavController().navigate(R.id.action_messages_to_chat, args)
                    }
                } else {
                    // 这里你可以用 Snackbar/Toast 提示失败原因
                }
            }
        )

        vb.recycler.adapter = adapter
        adapter.submitList(FakeRepository.listOrders())
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }
}