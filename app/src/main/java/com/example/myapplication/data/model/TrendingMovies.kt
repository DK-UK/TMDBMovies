package com.example.myapplication.data.model

data class TrendingMovies(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)