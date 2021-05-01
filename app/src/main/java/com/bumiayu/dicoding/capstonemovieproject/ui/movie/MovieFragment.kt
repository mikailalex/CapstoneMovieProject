package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.databinding.FragmentMovieBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : BaseFragment<FragmentMovieBinding>({ FragmentMovieBinding.inflate(it) }) {

    private val viewModel: MovieViewModel by viewModel()
    private val movieAdapter = MovieAdapter()

    override fun FragmentMovieBinding.onViewCreated(savedInstanceState: Bundle?) {
        val orientation = resources.configuration.orientation
        binding.apply {
            with(rvMovies) {
                layoutManager =
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                        context,
                        2
                    ) else GridLayoutManager(context, 4)
                setHasFixedSize(true)
                adapter = movieAdapter
            }
        }
        lifecycleScope.launch {
            viewModel.getMovies("Title").collectLatest { it.data?.let { it1 ->
                movieAdapter.submitData(it1)
                movieAdapter.notifyDataSetChanged()
                Log.d("aaaaaa", it1.toString())
            } }
        }
    }

    override fun observeViewModel() {}
}