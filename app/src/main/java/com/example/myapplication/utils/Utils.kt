package com.example.myapplication.utils

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
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

    fun NavController.safeNavigate(
        @IdRes currentDestinationId: Int,
        @IdRes id: Int,
        args: Bundle? = null
    ) {
        if (currentDestinationId == currentDestination?.id) {
            navigate(id, args)
        }
    }

    fun NavController.safeNavigate(direction: NavDirections) {
        Log.d("Dhaval", "Click happened")
        currentDestination?.getAction(direction.actionId)?.run {
            Log.d("Dhaval", "Click Propagated")
            navigate(direction)
        }
    }
}