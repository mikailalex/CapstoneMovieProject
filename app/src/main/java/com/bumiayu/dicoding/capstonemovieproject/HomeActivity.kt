package com.bumiayu.dicoding.capstonemovieproject

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseActivity
import com.bumiayu.dicoding.capstonemovieproject.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>({ ActivityHomeBinding.inflate(it) }) {

    private lateinit var navController: NavController

    override fun ActivityHomeBinding.onCreate(savedInstanceState: Bundle?) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_home_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavMain.setupWithNavController(navController)
    }

}