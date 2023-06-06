package com.example.myapplication.data.model.person

import com.example.myapplication.data.model.MovieResult

data class PersonMedia(
    val cast : List<MovieResult> = emptyList()
)
