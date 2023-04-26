package com.example.myapplication.ui.searchedItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.ui.home.HomeMoviesFragment
import com.example.myapplication.utils.ShareData
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SearchedItemFragment : Fragment() {

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_searched_item, container, false)

        tabLayout = view.findViewById(R.id.tab_searched_item)
        viewPager = view.findViewById(R.id.viewpager_searched_item)

        viewPager.adapter = SearchedPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = HomeMoviesFragment.searchedItemHashSet?.elementAt(position).toString()
        }.attach()

        return view
    }
}