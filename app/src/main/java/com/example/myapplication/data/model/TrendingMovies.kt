package com.example.myapplication.data.model

data class TrendingMovies(
    val page: Int = 0,
    val results: List<Result> = emptyList(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)