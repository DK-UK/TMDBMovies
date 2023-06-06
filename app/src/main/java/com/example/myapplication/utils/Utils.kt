package com.example.myapplication.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    public fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    public fun getScreenWidth() : Int{
        return Resources.getSystem().displayMetrics.widthPixels
    }

    public fun convertDate(dateStr : String, outputDateFormat : String) : String{
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())
            if (dateStr != null && dateStr.isNotEmpty()) {
                val date = inputFormat.parse(dateStr)
                return outputFormat.format(date)
            }
        }
        catch (e : Exception){
            return ""
        }
        return ""
    }

    fun formatDuration(duration: Int): String {
        val hours = duration / 60
        val minutes = duration % 60
        return String.format("%02d:%02d", hours, minutes)
    }

    fun appendImgPathToUrl(imgPath : String?) : String{
        return "https://image.tmdb.org/t/p/w780${imgPath}"
    }
}