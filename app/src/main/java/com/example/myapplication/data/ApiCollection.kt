package com.example.myapplication.data

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.model.TrendingMovies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCollection {


    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMovies(@Path("media_type") mediaType : String,
                                  @Path("time_window") timeWindow : String,
    @Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY) : TrendingMovies
}