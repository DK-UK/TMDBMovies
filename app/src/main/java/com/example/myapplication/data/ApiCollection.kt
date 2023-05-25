package com.example.myapplication.data

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.model.LatestTrailersModel
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.data.model.tv.TvDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCollection {


    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMovies(@Path("media_type") mediaType : String,
                                  @Path("time_window") timeWindow : String,
    @Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY, @Query("page") page : Int = 0) : TrendingMovies

    @GET("{media_type}/{popular_type}")
    suspend fun getPopularMovies(@Path("media_type") mediaType: String, @Path("popular_type") popularType : String, @Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY,
                                 @Query("sort_by") sortBy : String = "popularity.desc",
    @Query("with_watch_providers") withWatchProviders : Int = 8, @Query("page") page : Int = 1, @Query("vote_average.gte") voteAverage : Float = 7.5f) : TrendingMovies

    @GET("{mediaType}/{trailer_type}")
    suspend fun getLatestTrailer(@Path("mediaType") mediaType: String, @Path("trailer_type") trailerType : String, @Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY,
    @Query("language") language : String = "en-US", @Query("page") page : Int = 1) : TrendingMovies

    @GET("{media_type}/{id}/videos")
    suspend fun getLatestTrailerVideos(@Path("media_type") mediaType: String, @Path("id") movieId : Int, @Query("api_key") apiKey : String = BuildConfig.TMDB_API_KEY) : LatestTrailersModel

    @GET("search/multi")
    suspend fun getSearchedResults(@Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    @Query("query") query : String, @Query("page") page: Int = 1) : TrendingMovies

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movie_id : Int, @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    @Query("append_to_response") append_to_response : String = "videos,credits") : Movie

    @GET("tv/{tv_id}")
    suspend fun getTVDetails(@Path("tv_id") tv_id : Int, @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
                                @Query("append_to_response") append_to_response : String = "videos,credits") : TvDetails


    @GET("{media_type}/{media_id}/recommendations")
    suspend fun getMovieRecommendations(@Path("media_type") mediaType : String = "movie", @Path("media_id") movie_id : Int, @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
                               @Query("page") page: Int) : TrendingMovies

    // https://api.themoviedb.org/3/tv/78191?language=en-US
    //  https://api.themoviedb.org/3/movie/33/recommendations?api_key=&language=en-US&page=1
    //https://api.themoviedb.org/3/search/multi?api_key=568fb9ad3b36b1d871df2aa971f14d8c&language=en-US&query=ally&page=1&include_adult=false
    /*https://api.themoviedb.org/3/movie/now_playing?api_key=568fb9ad3b36b1d871df2aa971f14d8c&language=en-US&page=1*/
    /*https://api.themoviedb.org/3/discover/movie?api_key=568fb9ad3b36b1d871df2aa971f14d8c&sort_by=popularity.desc&with_watch_providers=8&media_type=movie*/
}