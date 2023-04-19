package com.example.myapplication.data

import com.example.myapplication.data.model.TrendingMovies

class MoviesRepository(val apiCollection: ApiCollection) {

    suspend fun getTrendingMovies(mediaType : String, timeWindow : String) : TrendingMovies {
            return apiCollection.getTrendingMovies(mediaType, timeWindow)
    }
    suspend fun getPopularMovies() : TrendingMovies{
        return apiCollection.getPopularMovies()
    }

    suspend fun getLatestTrailer(trailer_type : String) : TrendingMovies{
        return apiCollection.getLatestTrailer(trailerType = trailer_type)
    }
}