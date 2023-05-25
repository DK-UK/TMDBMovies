package com.example.myapplication.ui.tv

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.myapplication.R
import com.example.myapplication.data.ApiCollection
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.RetrofitHelper
import com.example.myapplication.data.model.HandleClicksModel
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.data.model.tv.TvDetails
import com.example.myapplication.ui.MoviesViewModel
import com.example.myapplication.ui.home.MoviesFactory
import com.example.myapplication.ui.home.TrendingMovieAdapter
import com.example.myapplication.ui.movie.CastAdapter
import com.example.myapplication.utils.ShareData
import com.example.myapplication.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TvSeriesFragment : Fragment() {

    private var tvDetails : TvDetails? = null
    private lateinit var recyclerMovieCast : RecyclerView
    private lateinit var castAdapter : CastAdapter

    private lateinit var recyclerTVSeriesRecommendation : RecyclerView
    private lateinit var tvSeriesRecommendationAdapter : TrendingMovieAdapter
    private lateinit var recyclerTVSeason : RecyclerView
    private lateinit var tVSeasonAdapter: TVSeasonAdapter
    private lateinit var viewModel: MoviesViewModel

    // recommendations
    private lateinit var recommendationLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ShareData.data != null){
            tvDetails = ShareData.data as TvDetails

            Log.e("Dhaval", "onCreate: tvDetails : ${tvDetails}", )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tv_series, container, false)

        val apiCollection: ApiCollection =
            RetrofitHelper.getRetrofit().create(ApiCollection::class.java)
        val moviesRepository = MoviesRepository(apiCollection)
        viewModel =
            ViewModelProvider(this, MoviesFactory(moviesRepository))[MoviesViewModel::class.java]

        // movie header layout
        val imgTVPoster : ImageView = view.findViewById(R.id.img_movie_poster)
        val txtTVTitle : TextView = view.findViewById(R.id.txt_movie_title)
        val txtTVTrailer : TextView = view.findViewById(R.id.txt_movie_trailer)
        val txtTVReleaseYear : TextView = view.findViewById(R.id.txt_movie_release_year)
        val progressTVRating : ProgressBar = view.findViewById(R.id.progress_movie_vote)
        val txtTVVote : TextView = view.findViewById(R.id.txt_movie_vote_perc)
        val txtTVDateAndRuntime : TextView = view.findViewById(R.id.txt_date_and_runtime)
        val txtTVGenre : TextView = view.findViewById(R.id.txt_movie_genre)
        val txtTVTagLine : TextView = view.findViewById(R.id.txt_movie_tagline)
        val txtTVOverview : TextView = view.findViewById(R.id.txt_movie_overview)
        val txtViewAllSeasons : TextView = view.findViewById(R.id.txt_view_all_seasons)
        val seasonLayout : LinearLayout = view.findViewById(R.id.season_layout)
        recommendationLayout = view.findViewById(R.id.recommendation_layout)

        val height = (Utils.getScreenWidth() / 2) - 50
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        imgTVPoster.layoutParams = params

        initTopBilledCast(view)

        initTVSeasons(view)

        tvDetails?.let { tv ->
            try {
                Glide.with(requireActivity())
                    .load(Utils.appendImgPathToUrl(tv.backdrop_path))
                    .error(R.drawable.ic_img_not_available)
                    .into(imgTVPoster)

                txtTVTitle.text = tv.original_name
                txtTVReleaseYear.text = "(${Utils.convertDate(tv.first_air_date, "yyyy")})"

                progressTVRating.max = 10
                progressTVRating.progress = tv.vote_average.toInt()
                progressTVRating.isIndeterminate = false

                txtTVTrailer.setOnClickListener {

                    CoroutineScope(Dispatchers.Main).launch {
                        val result =
                            viewModel.getLatestTrailerVideos("tv", tv.id).results.filter {
                                Log.e("Dhaval", "onCreateView: trailer video : ${it}", )
                                it.type.lowercase() == "trailer" || it.type.lowercase() == "teaser"
                            }

                        result.let {
                            if (it.size > 0){
                                val movieKey: String = result[0].key
                                movieKey.let { movieId ->
                                    redirectToYoutube(movieId)
                                }
                            }
                            else{
                                Toast.makeText(requireActivity(), "Trailer not available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                txtTVVote.text = "${(tv.vote_average * 10).toInt()}%"

                txtTVDateAndRuntime.text = "${
                    Utils.convertDate(
                        tv.first_air_date,
                        "dd/MM/yyyy"
                    )
                }"/* @ ${Utils.formatDuration(tv.episode_run_time.)}"*/

                txtTVGenre.text = tv.genres.joinToString {
                    it.name
                }

                txtTVTagLine.text = tv.tagline

                txtTVOverview.text = tv.overview

                castAdapter = CastAdapter(tv.credits.cast)
                recyclerMovieCast.adapter = castAdapter

                tv.seasons.let {

                    if (it.isNotEmpty()){
                        seasonLayout.visibility = View.VISIBLE
                        tVSeasonAdapter.refreshMovieList(tv.seasons.take(1))
                    }

                    if(it.size > 1){
                        txtViewAllSeasons.visibility = View.VISIBLE

                        txtViewAllSeasons.setOnClickListener {
                            if (tVSeasonAdapter.itemCount > 1){
                                txtViewAllSeasons.text = "Show All Seasons"
                                tVSeasonAdapter.refreshMovieList(tv.seasons.take(1))
                            }
                            else{
                                txtViewAllSeasons.text = "Hide Seasons"
                                tVSeasonAdapter.refreshMovieList(tv.seasons)
                            }
                        }
                    }
                    else{
                        txtViewAllSeasons.visibility = View.GONE
                    }
                }

                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getMovieRecommendations("tv", tv.id)
                }
            }
            catch (e : Exception){
                Log.e("Dhaval", "onCreateView: exception : ${e.toString()}", )
            }
        }

        initMovieRecommendations(view)

        return view
    }

    private fun initTVSeasons(view: View) {
        recyclerTVSeason = view.findViewById(R.id.recycler_tv_seasons)
        recyclerTVSeason.setHasFixedSize(true)
        recyclerTVSeason.layoutManager = LinearLayoutManager(requireContext())

        tVSeasonAdapter = TVSeasonAdapter(emptyList())
        recyclerTVSeason.adapter = tVSeasonAdapter
    }

    private fun initMovieRecommendations(view: View) {
        recyclerTVSeriesRecommendation = view.findViewById(R.id.recycler_movie_recommendations)
        recyclerTVSeriesRecommendation.setHasFixedSize(true)

        tvSeriesRecommendationAdapter = TrendingMovieAdapter(TrendingMovies()) {
            handleClicks(it)
        }
        recyclerTVSeriesRecommendation.adapter = tvSeriesRecommendationAdapter

        viewModel._movieRecommendationsLiveData.observe(requireActivity(), Observer {
            it?.let {
                Log.e("Dhaval", "initMovieRecommendations: ${it}", )

                if (it.results.isNotEmpty()) {
                    recommendationLayout.visibility = View.VISIBLE
                    tvSeriesRecommendationAdapter.refreshMovieList(it)
                }
            }
        })
    }

    private fun initTopBilledCast(view : View){
        recyclerMovieCast = view.findViewById(R.id.recycler_movie_cast)
        recyclerMovieCast.setHasFixedSize(true)
        recyclerMovieCast.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

    }

    private fun handleClicks(handleClicksModel: HandleClicksModel){
        Log.e("Dhaval", "handleClicks: ${handleClicksModel}", )

        try{
            val movieResult = handleClicksModel.modelClass as MovieResult

                val tvId : Int = movieResult.id
                if (tvId > 0){
                    CoroutineScope(Dispatchers.Main).launch {
                        val tvSeries : TvDetails = viewModel.getTVDetails(tvId)
                        ShareData.data = tvSeries

                        findNavController().navigate(R.id.action_tvSeriesFragment_self)
                    }
                }
        }
        catch (e : java.lang.Exception){
            Log.e("Dhaval", "handleClicks: exception : ${e.toString()}")
        }
    }

    private fun redirectToYoutube(movieId: String) {
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movieId))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.youtube");
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireActivity(), "You don't have Youtube installed", Toast.LENGTH_LONG)
                .show()
        }
    }


}