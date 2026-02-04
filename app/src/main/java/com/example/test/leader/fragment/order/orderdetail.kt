package com.example.test.leader.fragment.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.test.R
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.test.data.FakeRepository
import com.example.test.databinding.LeaderFragmentOrderdetailBinding

class OrderDetailFragment : Fragment(R.layout.leader_fragment_orderdetail) {

    private var _vb: LeaderFragmentOrderdetailBinding? = null
    private val vb get() = _vb!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _vb = LeaderFragmentOrderdetailBinding.bind(view)

        val orderId = requireArguments().getString("orderId")!!
        val order = FakeRepository.getOrder(orderId) ?: return

        vb.txtTitle.text = order.title
        vb.txtMeta.text = "${order.startTime} · ${order.from} → ${order.to}"
        vb.txtTags.text = order.tags.joinToString(" · ")
        vb.txtPrice.text = "${order.price} · ${order.peopleCount}人"

        // 图片占位：没有 url 则用内置占位图标
        vb.imgRoute.load(order.routeImageUrl) {
            placeholder(android.R.drawable.ic_menu_mapmode)
            error(android.R.drawable.ic_menu_mapmode)
        }

        vb.btnAccept.setOnClickListener {
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
                // TODO: Snackbar/Toast 显示 result.exceptionOrNull()?.message
            }
        }
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }
}