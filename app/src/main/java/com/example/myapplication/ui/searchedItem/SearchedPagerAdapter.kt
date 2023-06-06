package com.example.myapplication.ui.searchedItem

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.ui.home.HomeMoviesFragment

class SearchedPagerAdapter(private val fragment: Fragment, private val searchedItemData: TrendingMovies?) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return HomeMoviesFragment.searchedItem.searchedItemHashSet?.let {
            Log.e("Dhaval", "getItemCount: pager adapter size : ${it.size}", )
            it.size
        }!!
    }

    override fun createFragment(position: Int): Fragment {
        return SearchedListFragment.newInstance(HomeMoviesFragment.searchedItemHashSet.elementAt(position),
        searchedItemData)
    }
}