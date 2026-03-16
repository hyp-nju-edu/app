package com.example.test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityRoleSelectionBinding
import com.example.test.leader.LeaderMainActivity
import com.example.test.user.ActivityMainActivity
class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var vb: ActivityRoleSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(vb.root)

        // 从SharedPreferences获取用户信息
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val phone = prefs.getString("phone", "")
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        if (phone.isNullOrEmpty() || !isLoggedIn) {
            // 如果用户信息不完整，返回登录页面
            finish()
            return
        }

        // 显示用户信息
        vb.txtPhoneInfo.text = "手机号: $phone"

        // 设置卡片点击事件
        vb.cardLeader.setOnClickListener {
            saveUserRole("leader")
            startActivity(Intent(this, LeaderMainActivity::class.java))
            finish()
        }

        vb.cardUser.setOnClickListener {
            saveUserRole("user")
            startActivity(Intent(this, ActivityMainActivity::class.java))
            finish()
        }
    }

    private fun saveUserRole(role: String) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit()
            .putString("user_role", role)
            .apply()
    }
}