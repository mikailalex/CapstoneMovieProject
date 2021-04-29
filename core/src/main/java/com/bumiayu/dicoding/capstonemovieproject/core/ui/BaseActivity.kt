package com.bumiayu.dicoding.capstonemovieproject.core.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding>(val viewBinder: (LayoutInflater) -> T): AppCompatActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = viewBinder(layoutInflater)
        setContentView(binding.root)

        binding.onCreate(savedInstanceState)
        observeViewModel()
    }

    protected abstract fun T.onCreate(savedInstanceState: Bundle?)

    protected abstract fun observeViewModel()
}