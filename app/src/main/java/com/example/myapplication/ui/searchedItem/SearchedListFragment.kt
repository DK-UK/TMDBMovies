package com.example.myapplication.ui.searchedItem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.ApiCollection
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.RetrofitHelper
import com.example.myapplication.data.model.HandleClicksModel
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.data.model.tv.TvDetails
import com.example.myapplication.ui.MoviesViewModel
import com.example.myapplication.ui.home.MoviesFactory
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

    private lateinit var moviesViewModel: MoviesViewModel

    private var searchedItemData: TrendingMovies? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabName = it.getString(ARG_PARAM1)
            searchedItemData = it.getSerializable("data") as TrendingMovies
            Log.e("Dhaval", "onCreate: data : ${it.getSerializable("data")}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_searched_list, container, false)

        Log.e("Dhaval", "onCreateView: searchedListFragment")

        val apiCollection: ApiCollection =
            RetrofitHelper.getRetrofit().create(ApiCollection::class.java)
        val moviesRepository = MoviesRepository(apiCollection)
        moviesViewModel =
            ViewModelProvider(this, MoviesFactory(moviesRepository))[MoviesViewModel::class.java]


        val adapter = TrendingMovieAdapter(TrendingMovies()) { handleClickModel ->
            handleClicks(handleClickModel)
        }

        val recyclerSearchedItem = view.findViewById(R.id.recycler_searched_item) as RecyclerView
        recyclerSearchedItem.setHasFixedSize(true)
        recyclerSearchedItem.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerSearchedItem.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {


            val list = searchedItemData?.copy(results = searchedItemData?.results?.filter {
                Log.e("Dhaval", "onCreateView: type : ${it.media_type} -- ${it}")
                it.media_type == tabName
            } as MutableList<MovieResult>, type = Constant.TYPE_MOVIE)

            adapter.refreshMovieList(list!!)

        }
        return view
    }

    private fun handleClicks(handleClicksModel: HandleClicksModel): Unit {

        Log.e("Dhaval", "handleClicks: ${(handleClicksModel.modelClass as MovieResult).title}")

        val result = (handleClicksModel.modelClass as MovieResult)
        val mediaType = result.media_type
        val movieId: Int = result.id

        if (movieId > 0) {
            CoroutineScope(Dispatchers.Main).launch {

                if (mediaType != null && mediaType == "movie") {
                    val movie: Movie = moviesViewModel.getMovieDetails(movieId)
                    ShareData.data = movie

                    findNavController().navigate(R.id.action_searchedItemFragment_to_movieFragment)
                    Log.e("Dhaval", "handleClicks: movie : ${movie}")
                } else if (mediaType != null && mediaType == "tv") {
                    val tvSeries: TvDetails = moviesViewModel.getTVDetails(movieId)
                    ShareData.data = tvSeries

                    findNavController().navigate(R.id.action_searchedItemFragment_to_tvSeriesFragment)
                } else {
                    ShareData.data = result.id
                    findNavController().navigate(R.id.action_searchedItemFragment_to_personFragment)
                }
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(tabName: String, searchedItemData: TrendingMovies?) =
            SearchedListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, tabName)
                    putSerializable("data", searchedItemData)
                }
            }
    }

}