package com.example.test.leader.fragment.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.test.R
import com.example.test.data.FakeRepository
import com.example.test.databinding.LeaderFragmentOrderConfirmBinding

class OrderConfirmFragment : Fragment(R.layout.leader_fragment_order_confirm) {

    private var _vb: LeaderFragmentOrderConfirmBinding? = null
    private val vb get() = _vb!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _vb = LeaderFragmentOrderConfirmBinding.bind(view)

        val orderId = requireArguments().getString("orderId")!!
        val order = FakeRepository.getOrder(orderId) ?: return

        // 设置标题
        vb.toolbar.title = "接单确认"

        // 显示客户信息
        vb.txtCustomerName.text = order.customerName

        // 显示路线信息（暂时占位）
        vb.txtRouteDetail.text = "具体路线待确认，可与客户沟通"

        // 显示预估时间信息
        vb.txtEstimatedDuration.text = order.estimatedDuration
        vb.txtEstimatedStartTime.text = order.estimatedStartTime
        vb.txtEstimatedEndTime.text = order.estimatedEndTime

        // 接单按钮点击事件
        vb.btnConfirmAccept.setOnClickListener {
            val result = FakeRepository.acceptOrder(orderId)
            if (result.isSuccess) {
                val conv = FakeRepository.getConversationByOrder(orderId)
                if (conv != null) {
                    val args = Bundle().apply {
                        putString("conversationId", conv.id)
                        putString("orderId", orderId)
                    }
                    findNavController().navigate(R.id.action_messages_to_chat, args)
                }
            } else {
                // TODO: 显示错误提示
            }
        }

        // 返回按钮
        vb.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }
}
