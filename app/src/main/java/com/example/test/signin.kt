package com.example.test

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.test.databinding.ActivitySigninBinding
import com.example.test.leader.LeaderMainActivity
import com.example.test.RoleSelectionActivity
import com.example.test.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class signin : AppCompatActivity() {

    private lateinit var vb: ActivitySigninBinding
    private var countdownTimer: CountDownTimer? = null
    private var isCodeSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(vb.root)

        // 设置手机号输入监听
        vb.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // 当手机号变化时，重置验证码状态
                if (s?.length == 11) {
                    vb.btnGetCode.isEnabled = true
                } else {
                    vb.btnGetCode.isEnabled = false
                    if (countdownTimer != null) {
                        countdownTimer?.cancel()
                        countdownTimer = null
                        vb.btnGetCode.text = "获取验证码"
                        vb.btnGetCode.isEnabled = s?.length == 11
                    }
                }
            }
        })

        // 设置获取验证码按钮点击事件
        vb.btnGetCode.setOnClickListener {
            val phone = vb.etPhone.text.toString().trim()
            if (!validatePhone(phone)) return@setOnClickListener

            vb.btnGetCode.isEnabled = false

            ApiClient.pingApi.ping().enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val body = response.body()?.trim()
                    if (response.isSuccessful && body == "ok") {
                        Toast.makeText(this@signin, "服务器连接成功，验证码已发送（示范）", Toast.LENGTH_SHORT).show()
                        startCountdown()
                        vb.etCode.setText("123456")
                    } else {
                        vb.btnGetCode.isEnabled = true
                        Toast.makeText(this@signin, "服务器响应异常：${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    vb.btnGetCode.isEnabled = true
                    Toast.makeText(this@signin, "连接失败：${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 设置登录按钮点击事件
        vb.btnLogin.setOnClickListener {
            val phone = vb.etPhone.text.toString().trim()
            val code = vb.etCode.text.toString().trim()

            if (validateLogin(phone, code)) {
                // 登录成功，保存用户信息
                saveUserInfo(phone)
                // 清空输入框
                vb.etPhone.text?.clear()
                vb.etCode.text?.clear()
                // 重置获取验证码按钮状态
                vb.btnGetCode.text = "获取验证码"
                vb.btnGetCode.isEnabled = false
                // 跳转到角色选择页面
                val intent = Intent(this, RoleSelectionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

            }

    private fun validatePhone(phone: String): Boolean {
        if (phone.isEmpty()) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.length != 11) {
            Toast.makeText(this, "手机号必须是11位", Toast.LENGTH_SHORT).show()
            return false
        }

        // 简单验证是否为纯数字
        if (!phone.matches(Regex("^1[3-9]\\d{9}$"))) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun validateLogin(phone: String, code: String): Boolean {
        if (!validatePhone(phone)) {
            return false
        }

        if (code.isEmpty()) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show()
            return false
        }

        if (code.length != 6) {
            Toast.makeText(this, "验证码必须是6位", Toast.LENGTH_SHORT).show()
            return false
        }

        // TODO: 实现验证码验证逻辑
        // 这里暂时使用固定验证码 123456 进行演示
        if (code != "123456") {
            Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun startCountdown() {
        countdownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                vb.btnGetCode.text = "${millisUntilFinished / 1000}秒后重试"
            }

            override fun onFinish() {
                vb.btnGetCode.text = "获取验证码"
                vb.btnGetCode.isEnabled = vb.etPhone.text?.length == 11
                countdownTimer = null
            }
        }.start()
    }

    private fun saveUserInfo(phone: String) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit()
            .putString("phone", phone)
            .putBoolean("is_logged_in", true)
            .apply()
    }

    private fun saveUserRole(role: String) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit()
            .putString("user_role", role)
            .apply()
    }

    override fun onDestroy() {
        countdownTimer?.cancel()
        countdownTimer = null
        super.onDestroy()
    }
}