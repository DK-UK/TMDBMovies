package com.example.myapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.ApiCollection
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.RetrofitHelper
import com.example.myapplication.data.model.TrendingMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeMoviesFragment : Fragment() {

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var recyclerTrendingMovies: RecyclerView
    private lateinit var trendingMovieAdapter: TrendingMovieAdapter

    private lateinit var recyclerPopularMovies : RecyclerView
    private lateinit var popularMovieAdapter : TrendingMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_movies, container, false)


        val apiCollection: ApiCollection = RetrofitHelper.getRetrofit().create(ApiCollection::class.java)
        val moviesRepository = MoviesRepository(apiCollection)
        moviesViewModel = ViewModelProvider(this, MoviesFactory(moviesRepository))[MoviesViewModel::class.java]


        //region Trending Movies
        initTrendingMovies(view)
        val  trendingMovieAdapter = TrendingMovieAdapter(TrendingMovies())
        recyclerTrendingMovies.adapter = trendingMovieAdapter
        CoroutineScope(Dispatchers.Main).launch {
            moviesViewModel.getTrendingMovies("movie", "day").observe(requireActivity(), Observer {
                trendingMovieAdapter.refreshMovieList(it)
            })
        }
        //endregion

        //region Popular Movies
        initPopularMovies(view)
        val popularMovieAdapter = TrendingMovieAdapter(TrendingMovies())
        recyclerPopularMovies.adapter = popularMovieAdapter
        CoroutineScope(Dispatchers.Main).launch {
            moviesViewModel.getPopularMovies().observe(requireActivity(), Observer {
                popularMovieAdapter.refreshMovieList(it)
            })
        }
        //endregion

        return view
    }

    private fun initTrendingMovies(view : View) {
        trendingMovieAdapter = TrendingMovieAdapter(TrendingMovies())
        recyclerTrendingMovies = view.findViewById(R.id.recycler_trending_movies) as RecyclerView
        recyclerTrendingMovies.setHasFixedSize(true)
        recyclerTrendingMovies.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initPopularMovies(view : View) {
        popularMovieAdapter = TrendingMovieAdapter(TrendingMovies())
        recyclerPopularMovies = view.findViewById(R.id.recycler_popular_movies) as RecyclerView
        recyclerPopularMovies.setHasFixedSize(true)
        recyclerPopularMovies.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
    }
}