package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.ApiCollection
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.RetrofitHelper
import com.example.myapplication.ui.MoviesFactory
import com.example.myapplication.ui.MoviesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var moviesViewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiCollection: ApiCollection = RetrofitHelper.getRetrofit().create(ApiCollection::class.java)
        val moviesRepository = MoviesRepository(apiCollection)
        moviesViewModel = ViewModelProvider(this, MoviesFactory(moviesRepository))[MoviesViewModel::class.java]


        CoroutineScope(Dispatchers.Main).launch {

            moviesViewModel.getTrendingMovies("movie", "day")
        }
        moviesViewModel._moviesLiveData.observe(this, Observer {

            Log.e("Dhaval", "onCreate: TrendingMovies : ${it}", )
            it.results.forEach {
                Log.e("Dhaval", "onCreate: result : ${it.title} --- ${it.poster_path}", )
            }
        })
    }
}
