package com.example.test.leader

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.test.R
import com.example.test.databinding.LeaderActivityMain2Binding

class LeaderMainActivity : AppCompatActivity() {

    private lateinit var vb: LeaderActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = LeaderActivityMain2Binding.inflate(layoutInflater)
        setContentView(vb.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.leaderNavHost) as NavHostFragment
        val navController = navHost.navController

        vb.bottomNav.setupWithNavController(navController)
    }
}