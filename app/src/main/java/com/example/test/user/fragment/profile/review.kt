package com.example.test.user.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.test.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.data.Review
import com.example.test.databinding.LeaderFragmentReviewBinding

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.VH>(DIFF) {

    class VH(val vb: LeaderFragmentReviewBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vb = LeaderFragmentReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(vb)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.vb.txtUser.text = item.fromUserName
        holder.vb.rating.rating = item.rating
        holder.vb.txtContent.text = item.content
        holder.vb.txtTags.text = item.tags.joinToString(" · ")
        holder.vb.txtTime.text = item.time
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
        }
    }
}