package com.example.myapplication.data.model

import com.example.myapplication.utils.Constant
import java.io.Serializable

data class TrendingMovies(
    var type : Int = Constant.TYPE_MOVIE,
    var page: Int = 0,
    var results: MutableList<MovieResult> = mutableListOf(),
    var total_pages: Int = 0,
    var total_results: Int = 0
)
