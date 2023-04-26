package com.example.myapplication.ui.searchedItem

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.ui.home.HomeMoviesFragment

class SearchedPagerAdapter(private val fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return HomeMoviesFragment.searchedItem.searchedItemHashSet?.let {
            it.size
        }!!
    }

    override fun createFragment(position: Int): Fragment {
        return SearchedListFragment.newInstance(HomeMoviesFragment.searchedItemHashSet.elementAt(position))
    }
}