package com.example.myapplication.data

import com.example.myapplication.data.model.LatestTrailersModel
import com.example.myapplication.data.model.TrendingMovies

class MoviesRepository(val apiCollection: ApiCollection) {

    suspend fun getTrendingMovies(mediaType : String, timeWindow : String, page : Int) : TrendingMovies {
            return apiCollection.getTrendingMovies(mediaType, timeWindow, page = page)
    }
    suspend fun getPopularMovies(page : Int = 0) : TrendingMovies{
        return apiCollection.getPopularMovies(page = page)
    }

    suspend fun getLatestTrailer(trailer_type : String, page : Int = 0) : TrendingMovies{
        return apiCollection.getLatestTrailer(trailerType = trailer_type, page = page)
    }

    suspend fun getLatestTrailerVideos(movieId : Int) : LatestTrailersModel{
        return apiCollection.getLatestTrailerVideos(movieId)
    }
}