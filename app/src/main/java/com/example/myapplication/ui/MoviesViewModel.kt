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

    private val searchedResultsMutableLiveData = MutableLiveData(TrendingMovies())
    public val _seachedResultsLiveData : LiveData<TrendingMovies>
        get() = searchedResultsMutableLiveData

    fun getTrendingMovies(mediaType : String, timeWindow : String, page : Int = 1) {
        CoroutineScope(Dispatchers.Main).launch {
            trendingMoviesMutableLiveData.value = moviesRepository.getTrendingMovies(mediaType, timeWindow, page)
        }
    }

    fun getPopularMovies(mediaType: String = "movie", popularType : String = "now_playing", page : Int = 1) {
        CoroutineScope(Dispatchers.Main).launch {
            popularMoviesMutableLiveData.value = moviesRepository.getPopularMovies(mediaType = mediaType, popularType = popularType, page = page)
        }
    }

    fun getLatestTrailers(trailerType : String, mediaType : String = "movie"/*movie or tv*/, page: Int = 1){
        CoroutineScope(Dispatchers.Main).launch {
            latestTrailersMutableLiveData.value = moviesRepository.getLatestTrailer(trailer_type = trailerType, mediaType = mediaType, page = page)
        }
    }

    suspend fun getLatestTrailerVideos(movieId : Int) : LatestTrailersModel {
        return moviesRepository.getLatestTrailerVideos(movieId)
    }

    fun getSearchedResults(query : String, page: Int = 1) {
        CoroutineScope(Dispatchers.Main).launch {
            searchedResultsMutableLiveData.value = moviesRepository.getSearchedResults(query, page)
        }
    }

}