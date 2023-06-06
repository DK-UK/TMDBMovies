package com.example.myapplication.ui.home

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myapplication.R
import com.example.myapplication.data.model.HandleClicksModel
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class TrendingMovieAdapter(var trendingMoviesModel: TrendingMovies,
                           private val handleClick : (HandleClicksModel) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    class TrendingMoviesHolder(view : View) : ViewHolder(view){
        val moviePosterImg = view.findViewById(R.id.trending_movie_poster_image) as ImageView
        val txtMovieTitle = view.findViewById(R.id.txt_trending_movie_title) as TextView
        val txtMovieReleaseDate = view.findViewById(R.id.txt_trending_movie_release_date) as TextView
        val trendingMovieLayout = view.findViewById(R.id.trending_movie_layout) as LinearLayout
        val cardMovie = view.findViewById(R.id.card_movie) as CardView
    }

    class TrailersHolder(view : View) : ViewHolder(view){
        val trailerPosterImg = view.findViewById(R.id.latest_trailer_image) as ImageView
        val txtTrailerTitle = view.findViewById(R.id.txt_latest_trailer_title) as TextView
        val trailerLayout = view.findViewById(R.id.latest_trailer_layout) as LinearLayout
        val cardTrailers = view.findViewById(R.id.card_trailers) as CardView
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
                bindTrendingMovieViews(trendingMovieHolder, dataList, handleClick)
            }
            Constant.TYPE_TRAILER -> {
                val trailerHolder = holder as TrailersHolder
                bindTrailerViews(trailerHolder, dataList, handleClick)
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



    private fun bindTrendingMovieViews(
        holder: TrendingMoviesHolder,
        trendingMovie: MovieResult,
        handleClick: (HandleClicksModel) -> Unit,
    ) {
        val width = Utils.getScreenWidth() / 2 - 150
        val height = (width * 1.50).toInt()

        val layoutParams = LinearLayout.LayoutParams(width, height)
        holder.cardMovie.layoutParams = layoutParams

        Glide.with(holder.itemView.context)
            .load(Utils.appendImgPathToUrl(trendingMovie.poster_path))
            .error(R.drawable.ic_img_not_available)
            .into(holder.moviePosterImg)

        holder.txtMovieTitle.text = trendingMovie.title
        holder.txtMovieReleaseDate.text = if (trendingMovie.release_date != null) Utils.convertDate(trendingMovie.release_date, "dd MMM yy") else ""
        holder.trendingMovieLayout.setOnClickListener {
            handleClick(HandleClicksModel(type = holder.itemViewType, modelClass = trendingMovie))
        }
    }

    private fun bindTrailerViews(
        holder: TrailersHolder,
        trendingMovie: MovieResult,
        handleClick: (HandleClicksModel) -> Unit,
    ) {

        val width = Utils.getScreenWidth() / 2 + 160
        val height = (width) / 2 + 40

        val layoutParams = LinearLayout.LayoutParams(width, height)
        holder.cardTrailers.layoutParams = layoutParams

        Glide.with(holder.itemView.context)
            .load(Utils.appendImgPathToUrl(trendingMovie.backdrop_path))
            .error(R.drawable.ic_img_not_available)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val drawable = resource
                    Log.e("Dhaval", "onResourceReady: ${resource}")
                    drawable?.let {
                        trendingMovie.drawable = it
                        handleClick(HandleClicksModel(type = holder.itemViewType, modelClass = trendingMovie))
                    }
                    return false
                }

            })
            .into(holder.trailerPosterImg)

        holder.txtTrailerTitle.text = trendingMovie.title
        holder.trailerLayout.setOnClickListener {
            Log.e("Dhaval", "bindTrailerViews: ", )
            handleClick(HandleClicksModel(isItemClicked = true, type = holder.itemViewType, modelClass = trendingMovie))
        }

    }

    public fun refreshMovieList(trendingMoviesModel: TrendingMovies){
        Log.e("Dhaval", "refreshMovieList: ${trendingMoviesModel}", )
        this.trendingMoviesModel = trendingMoviesModel
        notifyDataSetChanged()
    }

   /* private fun convertDate(dateStr : String) : String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMM yy", Locale.getDefault())
        if (dateStr != null && dateStr.isNotEmpty()) {
            val date = inputFormat.parse(dateStr)
            return outputFormat.format(date)
        }
        return ""
    }*/
}