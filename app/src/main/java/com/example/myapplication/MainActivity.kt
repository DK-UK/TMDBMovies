package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.home.HomeMoviesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = HomeMoviesFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
           .add(R.id.home_fragment, fragment)
           .commit()
    }
}
