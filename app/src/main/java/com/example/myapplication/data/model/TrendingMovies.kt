package com.example.myapplication.data.model

import com.example.myapplication.utils.Constant

data class TrendingMovies(
    var type : Int = Constant.TYPE_MOVIE,
    var page: Int = 0,
    val results: List<MovieResult> = emptyList(),
    var total_pages: Int = 0,
    var total_results: Int = 0
)
