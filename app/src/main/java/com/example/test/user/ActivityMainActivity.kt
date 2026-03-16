package com.example.test.user

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.test.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity_main)

        // 初始化视图
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // 获取NavHostFragment - 注意ID必须和布局中的一致
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.UserNavHost) as NavHostFragment

        // 获取NavController
        val navController = navHostFragment.navController


        // 关联底部导航栏和NavController
        bottomNav.setupWithNavController(navController)
    }
}