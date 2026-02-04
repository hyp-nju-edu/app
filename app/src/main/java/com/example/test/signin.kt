package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.test.databinding.ActivitySigninBinding
import com.example.test.leader.LeaderMainActivity

class signin : AppCompatActivity() {

    private lateinit var vb: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.btnLogin.setOnClickListener {
            // 这里先不做真实登录，直接进入身份选择（也可直接默认领队）
            vb.roleGroup.visibility = android.view.View.VISIBLE
        }

        vb.btnEnterLeader.setOnClickListener {
            startActivity(Intent(this, LeaderMainActivity::class.java))
            finish()
        }

        vb.btnEnterUser.setOnClickListener {
            // 先不实现用户端完整主页：这里演示“身份切换存在”
            // 你后续可以做 UserMainActivity
            vb.txtHint.text = "用户端暂未实现，可返回选择领队端。"
        }
    }
}