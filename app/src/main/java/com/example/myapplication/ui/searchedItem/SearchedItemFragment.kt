package com.example.myapplication.ui.searchedItem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.ui.home.HomeMoviesFragment
import com.example.myapplication.utils.ShareData
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SearchedItemFragment : Fragment() {

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager2

    private var searchedItemData : TrendingMovies? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ShareData.data != null){
            try {
                searchedItemData = ShareData.data as TrendingMovies
            }
            catch(e : Exception){

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_searched_item, container, false)

        Log.e("Dhaval", "onCreateView: SearchedItemFragment", )
        tabLayout = view.findViewById(R.id.tab_searched_item)
        viewPager = view.findViewById(R.id.viewpager_searched_item)

        viewPager.adapter = SearchedPagerAdapter(this, searchedItemData)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = HomeMoviesFragment.searchedItemHashSet?.elementAt(position).toString()
        }.attach()

        return view
    }

}