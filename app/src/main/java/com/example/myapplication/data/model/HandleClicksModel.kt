package com.example.myapplication.data.model

import com.example.myapplication.utils.Constant

data class HandleClicksModel(
    val isItemClicked : Boolean = false,
    val type : Int = Constant.TYPE_MOVIE,
    val modelClass : Any = TrendingMovies()
)
