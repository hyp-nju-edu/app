package com.example.test.leader.fragment.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.data.Conversation
import com.example.test.databinding.LeaderFragmentCadapterBinding

class ConversationAdapter(
    private val onClick: (Conversation) -> Unit
) : ListAdapter<Conversation, ConversationAdapter.VH>(DIFF) {

    class VH(val vb: LeaderFragmentCadapterBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vb = LeaderFragmentCadapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(vb)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.vb.txtName.text = item.peerName
        holder.vb.txtLast.text = item.lastMessage
        holder.vb.txtTime.text = item.lastTime
        holder.vb.txtUnread.text = if (item.unreadCount > 0) item.unreadCount.toString() else ""
        holder.vb.root.setOnClickListener { onClick(item) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Conversation>() {
            override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation) = oldItem == newItem
        }
    }
}