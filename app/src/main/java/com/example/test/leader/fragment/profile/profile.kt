package com.example.test.leader.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.test.R
import android.content.Intent
import com.example.test.data.FakeRepository
import com.example.test.databinding.LeaderFragmentProfileBinding
import com.example.test.signin
import com.google.android.material.chip.Chip

class ProfileFragment : Fragment(R.layout.leader_fragment_profile) {

    private var _vb: LeaderFragmentProfileBinding? = null
    private val vb get() = _vb!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _vb = LeaderFragmentProfileBinding.bind(view)

        vb.txtName.text = "领队张三"
        vb.txtStats.text = "平均评分 4.9 · 接单 128"

        // 标签聚合计数
        val reviews = FakeRepository.listReviews()
        val tagCount = linkedMapOf<String, Int>()
        for (r in reviews) for (t in r.tags) tagCount[t] = (tagCount[t] ?: 0) + 1

        vb.chipGroup.removeAllViews()
        for ((tag, count) in tagCount) {
            val chip = Chip(requireContext()).apply {
                text = "$tag $count"
                isClickable = false
            }
            vb.chipGroup.addView(chip)
        }

        val adapter = ReviewAdapter()
        vb.recyclerReviews.adapter = adapter
        adapter.submitList(reviews)

        vb.btnSwitchRole.setOnClickListener {
            // 简化：回到登录页重新选身份
            startActivity(Intent(requireContext(), signin::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }
}