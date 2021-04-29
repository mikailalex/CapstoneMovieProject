package com.bumiayu.dicoding.capstonemovieproject.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumiayu.dicoding.capstonemovieproject.HomeActivity
import com.bumiayu.dicoding.capstonemovieproject.R

class SplashActivity : AppCompatActivity() {

    private val delayMillis = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val actionBar = supportActionBar
        actionBar?.hide()

        Handler(mainLooper).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, delayMillis)
    }
}