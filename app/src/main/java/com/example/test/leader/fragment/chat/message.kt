package com.example.test.leader.fragment.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test.R
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.test.data.Message
import com.example.test.data.MessageType
import com.example.test.databinding.LeaderLeftimgBinding
import com.example.test.databinding.LeaderFragmentRightimgBinding
import com.example.test.databinding.LeaderFragmentLeftstartBinding
import com.example.test.databinding.LeaderFragmentRightstartBinding

class MessageAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(DIFF) {

    override fun getItemViewType(position: Int): Int {
        val m = getItem(position)
        return when (m.type) {
            MessageType.TEXT -> if (m.isMe) 1 else 2
            MessageType.IMAGE -> if (m.isMe) 3 else 4
            MessageType.AUDIO -> if (m.isMe) 5 else 6 // 先占位
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> TextRightVH(LeaderFragmentRightstartBinding.inflate(inf, parent, false))
            2 -> TextLeftVH(LeaderFragmentLeftstartBinding.inflate(inf, parent, false))
            3 -> ImageRightVH(LeaderFragmentRightimgBinding.inflate(inf, parent, false))
            else -> ImageLeftVH(LeaderLeftimgBinding.inflate(inf, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val m = getItem(position)
        when (holder) {
            is TextRightVH -> holder.vb.txt.text = m.text
            is TextLeftVH -> holder.vb.txt.text = m.text
            is ImageRightVH -> holder.vb.img.load(m.imageUri)
            is ImageLeftVH -> holder.vb.img.load(m.imageUri)
        }
    }

    class TextRightVH(val vb: LeaderFragmentRightstartBinding) : RecyclerView.ViewHolder(vb.root)
    class TextLeftVH(val vb: LeaderFragmentLeftstartBinding) : RecyclerView.ViewHolder(vb.root)
    class ImageRightVH(val vb: LeaderFragmentRightimgBinding) : RecyclerView.ViewHolder(vb.root)
    class ImageLeftVH(val vb: LeaderLeftimgBinding) : RecyclerView.ViewHolder(vb.root)

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
        }
    }
}