package com.example.test.user.fragment.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import android.view.View
import com.example.test.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import com.example.test.data.FakeRepository
import com.example.test.databinding.LeaderFragmentProfileBinding
import com.example.test.signin
import com.google.android.material.chip.Chip
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment(R.layout.leader_fragment_profile) {

    private var _vb: LeaderFragmentProfileBinding? = null
    private val vb get() = _vb!!

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // 简化处理：直接使用选中的图片
            try {
                val inputStream = requireContext().contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                // 调整图片大小以适合头像显示
                val scaledBitmap = resizeBitmap(bitmap, 300)
                vb.ivAvatar.setImageBitmap(scaledBitmap)
                uploadAvatar(scaledBitmap)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "图片加载失败", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _vb = LeaderFragmentProfileBinding.bind(view)

        // 加载保存的头像
        loadSavedAvatar()

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

        vb.btnUploadAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        vb.btnViewReviews.setOnClickListener {
            // TODO: 实现查看所有评价的功能
            Toast.makeText(requireContext(), "查看所有评价功能待实现", Toast.LENGTH_SHORT).show()
        }

        vb.btnSwitchRole.setOnClickListener {
            // 简化：回到登录页重新选身份
            startActivity(Intent(requireContext(), signin::class.java))
            requireActivity().finish()
        }
    }

    private fun uploadAvatar(bitmap: Bitmap) {
        try {
            // 将 bitmap 转换为 Base64 字符串（模拟上传到服务器）
            val byteArray = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
            val base64Image = Base64.encodeToString(byteArray.toByteArray(), Base64.DEFAULT)

            // 模拟上传成功
            Toast.makeText(requireContext(), "头像已更新", Toast.LENGTH_SHORT).show()

            // 这里可以添加实际的上传逻辑，例如：
            // 1. 使用 Retrofit 或 OkHttp 上传到服务器
            // 2. 保存到 SharedPreferences 或数据库
            // 3. 更新用户资料信息
            saveAvatarToPreferences(base64Image)

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "头像更新失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }

        val scale = maxSize.toFloat() / maxOf(width, height)
        return Bitmap.createScaledBitmap(bitmap, (width * scale).toInt(), (height * scale).toInt(), true)
    }

    private fun loadSavedAvatar() {
        try {
            val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
            val base64Image = prefs.getString("avatar_base64", null)

            base64Image?.let {
                val byteArray = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                vb.ivAvatar.setImageBitmap(bitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveAvatarToPreferences(base64Image: String) {
        // 保存到 SharedPreferences
        val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putString("avatar_base64", base64Image).apply()
    }

    override fun onDestroyView() {
        _vb = null
        super.onDestroyView()
    }
}