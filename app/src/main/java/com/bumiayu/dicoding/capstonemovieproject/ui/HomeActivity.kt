package com.bumiayu.dicoding.capstonemovieproject.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumiayu.dicoding.capstonemovieproject.R
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseActivity
import com.bumiayu.dicoding.capstonemovieproject.databinding.ActivityHomeBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.movie.MovieViewModel
import com.bumiayu.dicoding.capstonemovieproject.ui.tv.TvShowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity<ActivityHomeBinding>({ ActivityHomeBinding.inflate(it) }) {

    private lateinit var navController: NavController

    //Base viewModel to share with fragments
    //Those variables are needed, don't delete
    private val viewModel: MovieViewModel by viewModel()
    private val viewModel2: TvShowViewModel by viewModel()

    override fun ActivityHomeBinding.onCreate(savedInstanceState: Bundle?) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_home_container) as NavHostFragment
            navController = navHostFragment.navController
            binding.bottomNavMain.setupWithNavController(navController)
    }
}