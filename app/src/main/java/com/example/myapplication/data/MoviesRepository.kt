package com.example.myapplication.data

import com.example.myapplication.data.model.LatestTrailersModel
import com.example.myapplication.data.model.TrendingMovies

class MoviesRepository(val apiCollection: ApiCollection) {

    suspend fun getTrendingMovies(mediaType : String, timeWindow : String, page : Int) : TrendingMovies {
            return apiCollection.getTrendingMovies(mediaType, timeWindow, page = page)
    }
    suspend fun getPopularMovies(mediaType: String, popularType : String, page : Int = 1) : TrendingMovies{
        return apiCollection.getPopularMovies(mediaType, popularType, page = page)
    }

    suspend fun getLatestTrailer(trailer_type : String, mediaType : String, page : Int = 1) : TrendingMovies{
        return apiCollection.getLatestTrailer(trailerType = trailer_type, mediaType = mediaType, page = page)
    }

    suspend fun getLatestTrailerVideos(movieId : Int) : LatestTrailersModel{
        return apiCollection.getLatestTrailerVideos(movieId)
    }
}