package com.example.myapplication.data.model

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName

data class MovieResult(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val media_type: String?,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val gender : Int,
    val known_for_department : String,
    @SerializedName(value = "poster_path", alternate = ["profile_path"])
    val poster_path: String,
    @SerializedName(value = "release_date", alternate = ["first_air_date"]) // for movie and tv type
    val release_date: String,
    @SerializedName(value = "title", alternate = ["name"])
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val known_for : List<MovieResult> = emptyList(), // for person type
    var drawable : Drawable? = null
)