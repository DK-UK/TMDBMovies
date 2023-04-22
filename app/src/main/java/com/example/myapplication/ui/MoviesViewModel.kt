package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.model.LatestTrailersModel
import com.example.myapplication.data.model.TrendingMovies

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

    suspend fun getTrendingMovies(mediaType : String, timeWindow : String) : LiveData<TrendingMovies>{
        trendingMoviesMutableLiveData.value = moviesRepository.getTrendingMovies(mediaType, timeWindow)
        return _trendingMoviesLiveData
    }

    suspend fun getPopularMovies() : LiveData<TrendingMovies> {
        popularMoviesMutableLiveData.value = moviesRepository.getPopularMovies()
        return _popularMoviesLiveData
    }

    suspend fun getLatestTrailers(trailerType : String) : LiveData<TrendingMovies> {
        latestTrailersMutableLiveData.value = moviesRepository.getLatestTrailer(trailerType)
        return _latestTrailerLiveData
    }

    suspend fun getLatestTrailerVideos(movieId : Int) : LatestTrailersModel {
        return moviesRepository.getLatestTrailerVideos(movieId)
    }

}