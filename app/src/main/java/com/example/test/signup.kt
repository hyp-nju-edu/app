package com.example.test  // 根据你的实际包名修改

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivitySignupBinding  // 根据你的实际包名修改

class signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用ViewBinding
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // 获取验证码按钮点击事件
        binding.getCode.setOnClickListener {
            val phone = binding.phoneNum.text.toString().trim()
            if (isValidPhone(phone)) {
                startCountdown()
                sendVerificationCode(phone)
            } else {
                showToast("请输入正确的11位手机号")
            }
        }

        // 注册按钮点击事件
        binding.register.setOnClickListener {
            if (validateInput()) {
                showRegisterConfirmDialog()
            }
        }

        // 取消按钮点击事件
        binding.cancel.setOnClickListener {
            showCancelConfirmationDialog()
        }

    }

    /**
     * 验证手机号格式
     */
    private fun isValidPhone(phone: String): Boolean {
        return phone.length == 11 && phone.matches(Regex("^1[3-9]\\d{9}$"))
    }

    /**
     * 验证所有输入
     */
    private fun validateInput(): Boolean {
        val username = binding.username.text.toString().trim()
        val phone = binding.phoneNum.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val code = binding.code.text.toString().trim()

        when {
            username.isEmpty() -> {
                showToast("请输入用户名")
                binding.username.requestFocus()
                return false
            }
            username.length < 2 -> {
                showToast("用户名至少2个字符")
                binding.username.requestFocus()
                return false
            }
            !isValidPhone(phone) -> {
                showToast("请输入正确的11位手机号")
                binding.phoneNum.requestFocus()
                return false
            }
            password.isEmpty() -> {
                showToast("请输入密码")
                binding.password.requestFocus()
                return false
            }
            password.length < 6 -> {
                showToast("密码至少6位")
                binding.password.requestFocus()
                return false
            }
            password.length > 20 -> {
                showToast("密码不能超过20位")
                binding.password.requestFocus()
                return false
            }
            code.isEmpty() -> {
                showToast("请输入验证码")
                binding.code.requestFocus()
                return false
            }
            code.length != 6 -> {
                showToast("请输入6位验证码")
                binding.code.requestFocus()
                return false
            }
        }
        return true
    }

    /**
     * 开始验证码倒计时
     */
    private fun startCountdown() {
        // 禁用按钮
        binding.getCode.isEnabled = false
        binding.getCode.alpha = 0.5f

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.getCode.text = "${seconds}秒后重试"
            }

            override fun onFinish() {
                resetCodeButton()
            }
        }.start()
    }

    /**
     * 重置验证码按钮
     */
    private fun resetCodeButton() {
        binding.getCode.isEnabled = true
        binding.getCode.alpha = 1.0f
        binding.getCode.text = "获取验证码"
        countDownTimer?.cancel()
        countDownTimer = null
    }

    /**
     * 发送验证码
     */
    private fun sendVerificationCode(phone: String) {
        // TODO: 这里调用你的API发送验证码
        // 模拟发送验证码
        Handler(Looper.getMainLooper()).postDelayed({
            showToast("验证码已发送到 $phone")
            // 模拟自动填充验证码（测试用）
            // binding.code.setText("123456")
        }, 1000)
    }

    /**
     * 执行注册
     */
    private fun performRegistration() {
        // TODO: 这里调用你的注册API
        val username = binding.username.text.toString().trim()
        val phone = binding.phoneNum.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val code = binding.code.text.toString().trim()

        // 显示加载状态
        binding.register.isEnabled = false
        binding.register.text = "注册中..."

        // 模拟网络请求
        Handler(Looper.getMainLooper()).postDelayed({
            // 注册成功
            binding.register.isEnabled = true
            binding.register.text = "注册"

            showToast("注册成功，欢迎 $username")

            // 返回结果到上一个页面
            val intent = Intent()
            intent.putExtra("registered_phone", phone)
            setResult(RESULT_OK, intent)

            // 关闭当前页面
            finish()
        }, 2000)
    }

    /**
     * 显示注册确认对话框
     */
    private fun showRegisterConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("确认注册")
            .setMessage("请确认手机号 ${binding.phoneNum.text} 是否正确？")
            .setPositiveButton("确认注册") { _, _ ->
                performRegistration()

                startActivity(Intent(this, RoleSelectionActivity::class.java))
            }
            .setNegativeButton("返回修改", null)
            .show()
    }

    /**
     * 显示取消确认对话框
     */
    private fun showCancelConfirmationDialog() {
        // 检查是否有输入内容
        val hasInput = binding.username.text.isNotEmpty() ||
                binding.phoneNum.text.isNotEmpty() ||
                binding.password.text.isNotEmpty() ||
                binding.code.text.isNotEmpty()

        if (hasInput) {
            AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要放弃注册吗？")
                .setPositiveButton("确定放弃") { _, _ ->
                    finish()
                }
                .setNegativeButton("继续注册", null)
                .show()
        } else {
            // 没有输入内容，直接返回
            finish()
        }
    }

    /**
     * 显示Toast消息
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 销毁时取消倒计时
     */
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}