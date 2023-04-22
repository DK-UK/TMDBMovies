package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.model.LatestTrailersModel
import com.example.myapplication.data.model.TrendingMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    private val trendingMoviesMutableLiveData = MutableLiveData(TrendingMovies())
    public val _trendingMoviesLiveData : LiveData<TrendingMovies>
    get() = trendingMoviesMutableLiveData

    private val popularMoviesMutableLiveData = MutableLiveData(TrendingMovies())
    public val _popularMoviesLiveData : LiveData<TrendingMovies>
        get() = popularMoviesMutableLiveData

    private val latestTrailersMutableLiveData = MutableLiveData(TrendingMovies())
    public val _latestTrailerLiveData : LiveData<TrendingMovies>
        get() = latestTrailersMutableLiveData

    fun getTrendingMovies(mediaType : String, timeWindow : String, page : Int = 1) {
        CoroutineScope(Dispatchers.Main).launch {
            trendingMoviesMutableLiveData.value = moviesRepository.getTrendingMovies(mediaType, timeWindow, page)
        }
    }

    fun getPopularMovies(page : Int = 1) {
        CoroutineScope(Dispatchers.Main).launch {
            popularMoviesMutableLiveData.value = moviesRepository.getPopularMovies(page = page)
        }
    }

    fun getLatestTrailers(trailerType : String, page: Int = 1){
        CoroutineScope(Dispatchers.Main).launch {
            latestTrailersMutableLiveData.value = moviesRepository.getLatestTrailer(trailerType, page = page)
        }
    }

    suspend fun getLatestTrailerVideos(movieId : Int) : LatestTrailersModel {
        return moviesRepository.getLatestTrailerVideos(movieId)
    }

}