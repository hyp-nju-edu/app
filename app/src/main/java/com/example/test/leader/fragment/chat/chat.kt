package com.example.test.leader.fragment.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.test.R
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.data.FakeRepository
import com.example.test.databinding.LeaderFragmentChatBinding

class ChatFragment : Fragment(R.layout.leader_fragment_chat) {

    private var _vb: LeaderFragmentChatBinding? = null
    private val vb get() = _vb!!

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val convId = requireArguments().getString("conversationId")!!
            FakeRepository.sendImage(convId, uri)
            refresh(convId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _vb = LeaderFragmentChatBinding.bind(view)

        val convId = requireArguments().getString("conversationId")!!
        val adapter = MessageAdapter()
        vb.recycler.adapter = adapter
        vb.recycler.layoutManager = LinearLayoutManager(requireContext())

        vb.btnSend.setOnClickListener {
            val text = vb.edtInput.text?.toString().orEmpty().trim()
            if (text.isNotEmpty()) {
                FakeRepository.sendText(convId, text)
                vb.edtInput.setText("")
                refresh(convId)
                vb.recycler.scrollToPosition(adapter.itemCount - 1)
            }
        }

        vb.btnImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        vb.btnVoice.setOnClickListener {
            // TODO: 语音：后续用 MediaRecorder + MediaPlayer 实现（需要 RECORD_AUDIO 权限）
        }

        refresh(convId)
    }

    private fun refresh(convId: String) {
        val list = FakeRepository.listMessages(convId)
        (vb.recycler.adapter as? MessageAdapter)?.submitList(list)
        if (list.isNotEmpty()) {
            vb.recycler.scrollToPosition(list.size - 1)
        }
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }
}