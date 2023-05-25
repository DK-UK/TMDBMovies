package com.example.myapplication.data

import com.example.myapplication.data.model.LatestTrailersModel
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.data.model.tv.TvDetails

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

    suspend fun getLatestTrailerVideos(mediaType : String, movieId : Int) : LatestTrailersModel{
        return apiCollection.getLatestTrailerVideos(mediaType, movieId)
    }

    suspend fun getSearchedResults(query : String, page: Int) : TrendingMovies{
        return apiCollection.getSearchedResults(query = query)
    }

    suspend fun getMovieDetails(movieId : Int) : Movie{
        return apiCollection.getMovieDetails(movieId)
    }

    suspend fun getTVDetails(tvId : Int) : TvDetails{
        return apiCollection.getTVDetails(tvId)
    }
    suspend fun getMovieRecommendations(mediaType : String, movieId: Int, page: Int) : TrendingMovies {
        return apiCollection.getMovieRecommendations(mediaType, movie_id = movieId, page = page)
    }
}