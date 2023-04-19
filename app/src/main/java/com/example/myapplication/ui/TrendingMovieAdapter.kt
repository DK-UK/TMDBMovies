package com.example.myapplication.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.utils.Constant
import java.text.SimpleDateFormat
import java.util.*

class TrendingMovieAdapter(var trendingMoviesModel : TrendingMovies) : RecyclerView.Adapter<ViewHolder>() {

    class TrendingMoviesHolder(view : View) : ViewHolder(view){
        val moviePosterImg = view.findViewById(R.id.trending_movie_poster_image) as ImageView
        val txtMovieTitle = view.findViewById(R.id.txt_trending_movie_title) as TextView
        val txtMovieReleaseDate = view.findViewById(R.id.txt_trending_movie_release_date) as TextView
        val trendingMovieLayout = view.findViewById(R.id.trending_movie_layout) as LinearLayout
    }

    class TrailersHolder(view : View) : ViewHolder(view){
        val trailerPosterImg = view.findViewById(R.id.latest_trailer_image) as ImageView
        val txtTrailerTitle = view.findViewById(R.id.txt_latest_trailer_title) as TextView
        val trailerLayout = view.findViewById(R.id.latest_trailer_layout) as LinearLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            Constant.TYPE_MOVIE -> TrendingMoviesHolder(LayoutInflater.from(parent.context).inflate(R.layout.trending_movies, parent, false))
            Constant.TYPE_TRAILER -> TrailersHolder(LayoutInflater.from(parent.context).inflate(R.layout.latest_trailers, parent, false))
            else -> throw java.lang.IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataList  = trendingMoviesModel.results.get(position)
        when (holder.itemViewType){
            Constant.TYPE_MOVIE -> {
                val trendingMovieHolder = holder as TrendingMoviesHolder
                bindTrendingMovieViews(trendingMovieHolder, dataList)
            }
            Constant.TYPE_TRAILER -> {
                val trailerHolder = holder as TrailersHolder
                bindTrailerViews(trailerHolder, dataList)
            }
        }
    }

    override fun getItemCount(): Int {
        return trendingMoviesModel.results.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (trendingMoviesModel.type){
            Constant.TYPE_MOVIE -> 0
            else -> 1
        }
    }



    private fun bindTrendingMovieViews(holder: TrendingMoviesHolder, trendingMovie: Result) {

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original${trendingMovie.poster_path}")
            .into(holder.moviePosterImg)

        holder.txtMovieTitle.text = trendingMovie.title
        holder.txtMovieReleaseDate.text = convertDate(trendingMovie.release_date)
        holder.trendingMovieLayout.setOnClickListener {
            Log.e("Dhaval", "bindTrendingMovieViews: clicked")
        }
    }

    private fun bindTrailerViews(holder: TrailersHolder, trendingMovie: Result) {

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original${trendingMovie.poster_path}")
            .into(holder.trailerPosterImg)

        holder.txtTrailerTitle.text = trendingMovie.title
        holder.trailerLayout.setOnClickListener {
            Log.e("Dhaval", "bindTrailerViews: ", )
        }
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