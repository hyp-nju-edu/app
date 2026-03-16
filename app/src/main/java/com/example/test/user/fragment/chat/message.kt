package com.example.test.user.fragment.chat

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
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
            is TextRightVH -> {
                holder.vb.txt.text = m.text
                holder.bindAvatar()
            }
            is TextLeftVH -> {
                holder.vb.txt.text = m.text
                holder.bindAvatar()
            }
            is ImageRightVH -> {
                holder.vb.img.load(m.imageUri)
                holder.bindAvatar()
            }
            is ImageLeftVH -> {
                holder.vb.img.load(m.imageUri)
                holder.bindAvatar()
            }
        }
    }

    class TextRightVH(val vb: LeaderFragmentRightstartBinding) : RecyclerView.ViewHolder(vb.root) {
        fun bindAvatar() {
            loadUserAvatar(vb.avatar, true)
        }
    }
    class TextLeftVH(val vb: LeaderFragmentLeftstartBinding) : RecyclerView.ViewHolder(vb.root) {
        fun bindAvatar() {
            loadUserAvatar(vb.avatar, false)
        }
    }
    class ImageRightVH(val vb: LeaderFragmentRightimgBinding) : RecyclerView.ViewHolder(vb.root) {
        fun bindAvatar() {
            loadUserAvatar(vb.avatar, true)
        }
    }
    class ImageLeftVH(val vb: LeaderLeftimgBinding) : RecyclerView.ViewHolder(vb.root) {
        fun bindAvatar() {
            loadUserAvatar(vb.avatar, false)
        }
    }

    companion object {
        fun loadUserAvatar(imageView: ImageView, isMyAvatar: Boolean) {
            if (isMyAvatar) {
                // 加载用户自己的头像
                val prefs = imageView.context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
                val base64Image = prefs.getString("avatar_base64", null)

                base64Image?.let {
                    try {
                        val byteArray = Base64.decode(it, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                        imageView.setImageBitmap(bitmap)
                        imageView.background = ContextCompat.getDrawable(imageView.context, R.drawable.avatar_background_my)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // 如果解码失败，使用默认头像
                        imageView.setImageResource(R.drawable.ic_my_avatar)
                        imageView.background = ContextCompat.getDrawable(imageView.context, R.drawable.avatar_background_my)
                    }
                } ?: run {
                    // 如果没有保存的头像，使用默认头像
                    imageView.setImageResource(R.drawable.ic_my_avatar)
                    imageView.background = ContextCompat.getDrawable(imageView.context, R.drawable.avatar_background_my)
                }
            } else {
                // 对方头像使用默认头像
                imageView.setImageResource(R.drawable.ic_default_avatar)
                imageView.background = ContextCompat.getDrawable(imageView.context, R.drawable.avatar_background)
            }
        }

        val DIFF = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
        }
    }
}

