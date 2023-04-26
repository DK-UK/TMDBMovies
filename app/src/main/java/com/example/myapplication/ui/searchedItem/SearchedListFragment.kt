package com.example.myapplication.ui.searchedItem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.model.HandleClicksModel
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.ui.home.TrendingMovieAdapter
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.ShareData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

class SearchedListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var tabName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_searched_list, container, false)

        val adapter = TrendingMovieAdapter(TrendingMovies()) { handleClickModel ->
            handleClicks(handleClickModel)
        }

        val recyclerSearchedItem = view.findViewById(R.id.recycler_searched_item) as RecyclerView
        recyclerSearchedItem.setHasFixedSize(true)
        recyclerSearchedItem.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerSearchedItem.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            val model : TrendingMovies = (ShareData.data as TrendingMovies)

            val list = model.copy(results = model.results.filter {
                it.media_type == tabName
            } as MutableList<MovieResult>)

            adapter.refreshMovieList(list)

        }
        return view
    }

    private fun handleClicks(handleClicksModel: HandleClicksModel): Unit {

        Log.e("Dhaval", "handleClicks: ${(handleClicksModel.modelClass as MovieResult).title}", )
    }


    companion object {

        @JvmStatic
        fun newInstance(tabName: String) =
            SearchedListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, tabName)
                }
            }
    }
}