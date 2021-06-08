package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.core.ui.ViewPagerAdapter
import com.bumiayu.dicoding.capstonemovieproject.databinding.FragmentMovieBaseBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.movie.MovieFragment.Companion.ARG_TAB_POSITION
import com.google.android.material.tabs.TabLayoutMediator

class MovieBaseFragment :
    BaseFragment<FragmentMovieBaseBinding>({ FragmentMovieBaseBinding.inflate(it) }) {

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun FragmentMovieBaseBinding.onViewCreated(savedInstanceState: Bundle?) {
        initAdapter()
    }

    private fun initAdapter() {
        val listFragment = listFragments()
        val tabTitle = listOf("Search", "Popular", "In Theater", "Top Rated", "Other")

        binding?.viewPager?.adapter =
            ViewPagerAdapter(listFragment, childFragmentManager, viewLifecycleOwner.lifecycle)

        binding?.apply {
            tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitle[position]
            }
        }
        tabLayoutMediator.attach()
    }

    private fun listFragments(): List<Fragment> {
        val listFragments = ArrayList<Fragment>()
        for (i in 1..5) {
            val movieFragment = MovieFragment()
            movieFragment.arguments = Bundle().apply {
                putInt(ARG_TAB_POSITION, i)
            }
            listFragments.add(movieFragment)
        }
        return listFragments
    }

    override fun onDestroyView() {
        binding?.viewPager?.adapter = null
        tabLayoutMediator.detach()
        super.onDestroyView()
    }
}