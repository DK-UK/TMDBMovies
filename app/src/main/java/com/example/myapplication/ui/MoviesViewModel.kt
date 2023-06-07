package com.example.myapplication.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.model.LatestTrailersModel
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.data.model.person.PersonDetails
import com.example.myapplication.data.model.person.PersonMedia
import com.example.myapplication.data.model.tv.TvDetails
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

    private val movieDetailsMutableLiveData : MutableLiveData<MovieResult> = MutableLiveData()
    public val _movieDetailsLiveData : LiveData<MovieResult>
        get() = movieDetailsMutableLiveData

    private val movieRecommendationsutableLiveData : MutableLiveData<TrendingMovies> = MutableLiveData()
    public val _movieRecommendationsLiveData : LiveData<TrendingMovies>
        get() = movieRecommendationsutableLiveData

    private val personDetailsLiveData : MutableLiveData<PersonDetails> = MutableLiveData()
    public val _personDetailsLiveData : LiveData<PersonDetails>
        get() = personDetailsLiveData

    private val personMediaMediaListLiveData : MutableLiveData<PersonMedia> = MutableLiveData()
    public val _personMediaMediaListLiveData : LiveData<PersonMedia>
        get() = personMediaMediaListLiveData


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

    suspend fun getLatestTrailerVideos(mediaType : String, movieId : Int) : LatestTrailersModel {
        return moviesRepository.getLatestTrailerVideos(mediaType, movieId)
    }

    suspend fun getSearchedResults(query : String, page: Int = 1): TrendingMovies {
        Log.e("Dhaval", "getSearchedResults: req", )
            return moviesRepository.getSearchedResults(query, page)

       /* CoroutineScope(Dispatchers.Main).launch {
            searchedResultsMutableLiveData.value = moviesRepository.getSearchedResults(query, page)
            Log.e("Dhaval", "getSearchedResults: response : ${searchedResultsMutableLiveData.value}", )
        }*/
    }

    suspend fun getMovieDetails(movieId : Int): Movie {
        return moviesRepository.getMovieDetails(movieId)
//        CoroutineScope(Dispatchers.Main).launch {
//            movieDetailsMutableLiveData.value = moviesRepository.getMovieDetails(movieId)
//        }
    }

    suspend fun getTVDetails(tvId : Int) : TvDetails{
        return moviesRepository.getTVDetails(tvId)
    }

    suspend fun getMovieRecommendations(mediaType : String, movieId: Int, page: Int = 1) {
        movieRecommendationsutableLiveData.value = moviesRepository.getMovieRecommendations(mediaType, movieId, page)
    }

    suspend fun getPersonDetails(personId : Int) {
        personDetailsLiveData.value = moviesRepository.getPersonDetails(personId)
    }

    suspend fun getPersonMediaList(personId : Int) {
        personMediaMediaListLiveData.value = moviesRepository.getPersonMediaList(personId)
    }
}