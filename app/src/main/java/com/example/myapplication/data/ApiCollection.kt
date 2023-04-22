package com.example.myapplication.data

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.model.LatestTrailersModel
import com.example.myapplication.data.model.TrendingMovies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCollection {


    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMovies(@Path("media_type") mediaType : String,
                                  @Path("time_window") timeWindow : String,
    @Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY) : TrendingMovies

    @GET("discover/movie")
    suspend fun getPopularMovies(@Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY, @Query("sort_by") sortBy : String = "popularity.desc",
    @Query("with_watch_providers") withWatchProviders : Int = 8) : TrendingMovies

    @GET("movie/{trailer_type}")
    suspend fun getLatestTrailer(@Path("trailer_type") trailerType : String, @Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY,
    @Query("language") language : String = "en-US", @Query("page") page : Int = 1) : TrendingMovies

    @GET("movie/{id}/videos")
    suspend fun getLatestTrailerVideos(@Path("id") movieId : Int, @Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY) : LatestTrailersModel


    /*https://api.themoviedb.org/3/movie/now_playing?api_key=568fb9ad3b36b1d871df2aa971f14d8c&language=en-US&page=1*/
    /*https://api.themoviedb.org/3/discover/movie?api_key=568fb9ad3b36b1d871df2aa971f14d8c&sort_by=popularity.desc&with_watch_providers=8&media_type=movie*/
}