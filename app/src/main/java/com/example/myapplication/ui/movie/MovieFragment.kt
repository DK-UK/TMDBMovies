package com.example.myapplication.ui.movie

import android.widget.LinearLayout.LayoutParams
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Movie
import com.example.myapplication.ui.home.TrendingMovieAdapter
import com.example.myapplication.utils.ShareData
import com.example.myapplication.utils.Utils
import org.w3c.dom.Text

class MovieFragment : Fragment() {

    private var movieDetails : Movie? = null
    private lateinit var recyclerMovieCast : RecyclerView
    private lateinit var castAdapter : TrendingMovieAdapter

    private lateinit var recyclerMovieRecommendation : RecyclerView
    private lateinit var movieRecommendationAdapter : TrendingMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ShareData.data != null){
            movieDetails = ShareData.data as Movie
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movie, container, false)

//        val toolbar = view.findViewById(R.id.main_toolbar) as Toolbar
//        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back, resources.newTheme())
//        toolbar.setNavigationOnClickListener {

//        }
        // movie header layout
        val imgMoviePoster : ImageView = view.findViewById(R.id.img_movie_poster)
        val txtMovieTitle : TextView = view.findViewById(R.id.txt_movie_title)
        val txtMovieReleaseYear : TextView = view.findViewById(R.id.txt_movie_release_year)
        val progressMovieRating : ProgressBar = view.findViewById(R.id.progress_movie_vote)
        val txtMovieVote : TextView = view.findViewById(R.id.txt_movie_vote_perc)
        val txtMovieDateAndRuntime : TextView = view.findViewById(R.id.txt_date_and_runtime)
        val txtMovieGenre : TextView = view.findViewById(R.id.txt_movie_genre)
        val txtMovieTagLine : TextView = view.findViewById(R.id.txt_movie_tagline)
        val txtMovieOverview : TextView = view.findViewById(R.id.txt_movie_overview)

        val imgWidth = (Utils.getScreenWidth() / 2) - 50
        val params = LayoutParams(MATCH_PARENT, imgWidth)
        imgMoviePoster.layoutParams = params

        movieDetails?.let {movie ->
            try {
                Glide.with(requireActivity())
                    .load(Utils.appendImgPathToUrl(movie.backdrop_path))
                    .error(R.drawable.ic_img_not_available)
                    .into(imgMoviePoster)

                txtMovieTitle.text = movie.title
                txtMovieReleaseYear.text = "(${Utils.convertDate(movie.release_date, "yyyy")})"

                progressMovieRating.max = 10
                progressMovieRating.progress = movie.vote_average.toInt()
                progressMovieRating.isIndeterminate = false

                txtMovieVote.text = "${(movie.vote_average * 10).toInt()}%"

                txtMovieDateAndRuntime.text = "${
                    Utils.convertDate(
                        movie.release_date,
                        "dd/MM/yyyy"
                    )
                } @ ${Utils.formatDuration(movie.runtime)}"

                txtMovieGenre.text = movie.genres.joinToString {
                    it.name
                }

                txtMovieTagLine.text = movie.tagline

                txtMovieOverview.text = movie.overview
            }
            catch (e : java.lang.Exception){
                Log.e("Dhaval", "onCreateView: exception : ${e.toString()}", )
            }
        }
        return view
    }

    private fun initTopBilledCast(view : View){
        recyclerMovieCast = view.findViewById(R.id.recycler_movie_cast)
        recyclerMovieCast.setHasFixedSize(true)
        recyclerMovieCast.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

    }
}