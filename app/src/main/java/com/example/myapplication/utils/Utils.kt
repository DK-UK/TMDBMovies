package com.example.myapplication.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log


object Utils {

    public fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    public fun getScreenWidth() : Int{
        return Resources.getSystem().displayMetrics.widthPixels
    }
}