package com.example.myapplication.ui.person

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.R
import com.example.myapplication.data.model.HandleClicksModel
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.person.PersonMedia
import com.example.myapplication.utils.Utils

class PersonCareerListAdapter(
    private var personMediaCareerList: PersonMedia,
    private val handleClick: (MovieResult) -> Unit
) :
    RecyclerView.Adapter<PersonCareerListAdapter.viewHolder>() {

    public class viewHolder(private val view: View) : ViewHolder(view) {
        val txtReleaseYear: TextView = view.findViewById(R.id.txt_year)
        val txtTitle: TextView = view.findViewById(R.id.txt_title)
        val episodesDone: TextView = view.findViewById(R.id.txt_episodes)
        val character: TextView = view.findViewById(R.id.txt_character_as)
        val personCareerLayout : RelativeLayout = view.findViewById(R.id.person_career_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.person_career_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val personCareer = personMediaCareerList.cast[position]
        bind(holder, personCareer)
    }

    override fun getItemCount(): Int {
        return personMediaCareerList.cast.size
    }

    public fun bind(holder: viewHolder, personCareer: MovieResult) {

        try {
            holder.txtReleaseYear.text = personCareer.release_date.let {
                if (it != null && it.isNotEmpty()) {
                    Utils.convertDate(it, "yyyy")
                } else {
                    "-"
                }
            }

            holder.txtTitle.text = personCareer.title
            holder.episodesDone.text =
                if (personCareer.episode_count > 0) "(${personCareer.episode_count} episodes)" else ""

            holder.character.text = "as ${personCareer.character}"

            holder.personCareerLayout.setOnClickListener {
                handleClick(personCareer)
            }
        }
        catch (e : Exception){

        }
}

public fun refreshList(personMedia: PersonMedia) {
    this.personMediaCareerList = personMedia
    notifyDataSetChanged()
}
}