package com.example.myapplication.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.model.TrendingMovies
import java.text.SimpleDateFormat
import java.util.*

class TrendingMovieAdapter(var trendingMoviesModel : TrendingMovies) : RecyclerView.Adapter<TrendingMovieAdapter.viewHolder>() {

    class viewHolder(view : View) : ViewHolder(view){
        val moviePosterImg = view.findViewById(R.id.trending_movie_poster_image) as ImageView
        val txtMovieTitle = view.findViewById(R.id.txt_trending_movie_title) as TextView
        val txtMovieReleaseDate = view.findViewById(R.id.txt_trending_movie_release_date) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trending_movies, parent, false)
        val viewHolder = viewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return trendingMoviesModel.results.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val trendingMovie = trendingMoviesModel.results.get(position)
        bindView(holder, trendingMovie)
    }

    private fun bindView(holder: viewHolder, trendingMovie: Result) {

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original${trendingMovie.poster_path}")
            .into(holder.moviePosterImg)

        holder.txtMovieTitle.text = trendingMovie.title
        holder.txtMovieReleaseDate.text = convertDate(trendingMovie.release_date)
    }

    public fun refreshMovieList(trendingMoviesModel: TrendingMovies){
        this.trendingMoviesModel = trendingMoviesModel
        notifyDataSetChanged()
    }

    private fun convertDate(dateStr : String) : String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMM yy", Locale.getDefault())

        val date = inputFormat.parse(dateStr)
        return outputFormat.format(date)
    }
}