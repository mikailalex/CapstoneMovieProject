package com.bumiayu.dicoding.favorite

import android.os.Bundle
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.core.ui.ViewPagerAdapter
import com.bumiayu.dicoding.favorite.databinding.FragmentFavoriteBaseBinding
import com.bumiayu.dicoding.favorite.movie.FavoriteMovieFragment
import com.bumiayu.dicoding.favorite.tv.FavoriteTvShowFragment
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteBaseFragment :
    BaseFragment<FragmentFavoriteBaseBinding>({ FragmentFavoriteBaseBinding.inflate(it) }) {

    override fun FragmentFavoriteBaseBinding.onViewCreated(savedInstanceState: Bundle?) {
        initViewPager()
    }

    private fun initViewPager() {
        val listFragment = listOf(FavoriteMovieFragment(), FavoriteTvShowFragment())
        val tabTitle = listOf("Movie", "Tv Show")

        binding?.viewPager?.adapter =
            ViewPagerAdapter(listFragment, childFragmentManager, lifecycle)

        binding?.apply {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitle[position]
            }.attach()
        }
    }
}