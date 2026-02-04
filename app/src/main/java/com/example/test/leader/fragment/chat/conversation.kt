package com.example.test.leader.fragment.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.test.R
import com.example.test.data.FakeRepository
import com.example.test.databinding.LeaderFragmentConversationBinding

class ConversationListFragment : Fragment(R.layout.leader_fragment_conversation) {

    private var _vb: LeaderFragmentConversationBinding? = null
    private val vb get() = _vb!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _vb = LeaderFragmentConversationBinding.bind(view)

        val adapter = ConversationAdapter { conv ->
            val args = Bundle().apply {
                putString("conversationId", conv.id)
                putString("orderId", conv.orderId)
            }
            findNavController().navigate(R.id.action_messages_to_chat, args)
        }
        vb.recycler.adapter = adapter
        adapter.submitList(FakeRepository.listConversations())
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }
}