package com.example.myapplication.ui.tv

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.data.model.tv.Season
import com.example.myapplication.utils.Utils

class TVSeasonAdapter(private var tvSeasons : List<Season>) : RecyclerView.Adapter<TVSeasonAdapter.viewHolder>() {

    public class viewHolder(val view : View) : ViewHolder(view) {
        val imgPoster : ImageView = view.findViewById(R.id.season_poster_img)
        val seasonName : TextView = view.findViewById(R.id.txt_season_name)
        val seasonReleaseYear : TextView = view.findViewById(R.id.txt_season_release_year)
        val seasonEpisodeCount : TextView = view.findViewById(R.id.txt_season_episodes)
        val seasonOverview : TextView = view.findViewById(R.id.txt_season_overview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tv_season, parent, false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val season = tvSeasons[position]
        bind(holder, season)
    }

    private fun bind(holder: viewHolder, season: Season) {
        val width = Utils.getScreenWidth() / 2 - 100
        val height = (width * 1.25).toInt()

        val layoutParams = RelativeLayout.LayoutParams(width, height)
        holder.imgPoster.layoutParams = layoutParams

        Glide.with(holder.itemView.context)
            .load(Utils.appendImgPathToUrl(season.poster_path))
            .error(R.drawable.ic_img_not_available)
            .into(holder.imgPoster)

        holder.seasonName.text = season.name
        holder.seasonReleaseYear.text = season.air_date?.let {
            Utils.convertDate(it, "yyyy")
        }

        holder.seasonEpisodeCount.text = "| ${season.episode_count} Episodes"
        holder.seasonOverview.text = season.overview
    }

    override fun getItemCount(): Int {
        return tvSeasons.size
    }

    public fun refreshMovieList(seasons : List<Season>){
        this.tvSeasons = seasons
        notifyDataSetChanged()
    }
}