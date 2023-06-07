package com.example.myapplication.ui.movie

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
import com.example.myapplication.R
import com.example.myapplication.data.model.Cast
import com.example.myapplication.data.model.HandleClicksModel
import com.example.myapplication.utils.Utils
import okhttp3.internal.http.ExchangeCodec

class CastAdapter(val casts : List<Cast>,
                  private val handleClick : (HandleClicksModel) -> Unit) : RecyclerView.Adapter<CastAdapter.viewHolder>() {

    public class viewHolder(view: View) : ViewHolder(view){
        val castPosterImg = view.findViewById(R.id.cast_img) as ImageView
        val txtCastName = view.findViewById(R.id.txt_cast_name) as TextView
        val txtCastCharacterName = view.findViewById(R.id.txt_character_name) as TextView
//        val txtCaracterEpisodes = view.findViewById(R.id.txt_character_episodes) as TextView
        val trendingMovieLayout = view.findViewById(R.id.cast_layout) as LinearLayout
        val cardMovie = view.findViewById(R.id.card_cast) as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return casts.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        try {
            val cast = casts[position]

            Log.e("Dhaval", "onBindViewHolder: cast : ${cast}",)
            val width = Utils.getScreenWidth() / 2 - 200
            val height = (width * 1.50).toInt()

            val layoutParams = LinearLayout.LayoutParams(width, height)
            holder.cardMovie.layoutParams = layoutParams

            Glide.with(holder.itemView.context)
                .load(Utils.appendImgPathToUrl(cast.profile_path))
                .error(R.drawable.ic_img_not_available)
                .into(holder.castPosterImg)

            holder.txtCastName.text = cast.name
            holder.txtCastCharacterName.text = cast.character
//        holder.txtCaracterEpisodes.text = ""

            holder.cardMovie.setOnClickListener {
                handleClick(HandleClicksModel(false, 0, cast))
            }
        }
        catch (e : java.lang.Exception){

        }
    }
}