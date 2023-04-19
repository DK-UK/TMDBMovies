package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.model.TrendingMovies

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    private val moviesMutableLiveData = MutableLiveData(TrendingMovies(0, emptyList(), 0, 0))
    public val _moviesLiveData : LiveData<TrendingMovies>
    get() = moviesMutableLiveData

    suspend fun getTrendingMovies(mediaType : String, timeWindow : String){
        moviesMutableLiveData.value = moviesRepository.getTrendingMovies(mediaType, timeWindow)
    }

}