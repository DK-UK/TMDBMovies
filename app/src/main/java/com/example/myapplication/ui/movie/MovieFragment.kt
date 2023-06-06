package com.example.myapplication.ui.movie

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.LinearLayout.LayoutParams
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.appcompat.widget.Toolbar
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
import com.example.myapplication.data.model.*
import com.example.myapplication.data.model.tv.TvDetails
import com.example.myapplication.ui.MoviesViewModel
import com.example.myapplication.ui.home.MoviesFactory
import com.example.myapplication.ui.home.TrendingMovieAdapter
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.ShareData
import com.example.myapplication.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class MovieFragment : Fragment() {

    private var movieDetails : Movie? = null
    private lateinit var recyclerMovieCast : RecyclerView
    private lateinit var castAdapter : CastAdapter

    private lateinit var recyclerMovieRecommendation : RecyclerView
    private lateinit var movieRecommendationAdapter : TrendingMovieAdapter
    private lateinit var viewModel: MoviesViewModel

    // recommendations
    private lateinit var recommendationLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ShareData.data != null){
            movieDetails = ShareData.data as Movie
        }

        Log.e("Dhaval", "onCreate: MovieFragment : ${movieDetails}", )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movie, container, false)

        Log.e("Dhaval", "onCreateView: MovieFragment", )

        val apiCollection: ApiCollection =
            RetrofitHelper.getRetrofit().create(ApiCollection::class.java)
        val moviesRepository = MoviesRepository(apiCollection)
        viewModel =
            ViewModelProvider(this, MoviesFactory(moviesRepository))[MoviesViewModel::class.java]

        // movie header layout
        val imgMoviePoster : ImageView = view.findViewById(R.id.img_movie_poster)
        val txtMovieTitle : TextView = view.findViewById(R.id.txt_movie_title)
        val txtMovieTrailer : TextView = view.findViewById(R.id.txt_movie_trailer)
        val txtMovieReleaseYear : TextView = view.findViewById(R.id.txt_movie_release_year)
        val progressMovieRating : ProgressBar = view.findViewById(R.id.progress_movie_vote)
        val txtMovieVote : TextView = view.findViewById(R.id.txt_movie_vote_perc)
        val txtMovieDateAndRuntime : TextView = view.findViewById(R.id.txt_date_and_runtime)
        val txtMovieGenre : TextView = view.findViewById(R.id.txt_movie_genre)
        val txtMovieTagLine : TextView = view.findViewById(R.id.txt_movie_tagline)
        val txtMovieOverview : TextView = view.findViewById(R.id.txt_movie_overview)
        recommendationLayout = view.findViewById(R.id.recommendation_layout)

        val imgWidth = (Utils.getScreenWidth() / 2) - 50
        val params = LayoutParams(MATCH_PARENT, imgWidth)
        imgMoviePoster.layoutParams = params

        initTopBilledCast(view)

        movieDetails?.let {movie ->
            try {
                Glide.with(requireActivity())
                    .load(Utils.appendImgPathToUrl(movie.backdrop_path))
                    .error(R.drawable.ic_img_not_available)
                    .into(imgMoviePoster)

                txtMovieTitle.text = movie.title
                txtMovieReleaseYear.text = "(" + movie.release_date.let {
                    Utils.convertDate(movie.release_date, "yyyy")
                }.toString() + ")"

                progressMovieRating.max = 10
                progressMovieRating.progress = movie.vote_average.toInt()
                progressMovieRating.isIndeterminate = false

                txtMovieTrailer.setOnClickListener {

                    CoroutineScope(Dispatchers.Main).launch {
                        val result =
                            viewModel.getLatestTrailerVideos("movie", movie.id).results.filter {
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

                castAdapter = CastAdapter(movie.credits.cast){
                    handleClicks(it)
                }
                recyclerMovieCast.adapter = castAdapter

                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getMovieRecommendations("movie", movie.id)
                }
            }
            catch (e : java.lang.Exception){
                Log.e("Dhaval", "onCreateView: exception : ${e.toString()}", )
            }
        }

        initMovieRecommendations(view)


        return view
    }

    private fun initMovieRecommendations(view: View) {
        recyclerMovieRecommendation = view.findViewById(R.id.recycler_movie_recommendations)
        recyclerMovieRecommendation.setHasFixedSize(true)

        movieRecommendationAdapter = TrendingMovieAdapter(TrendingMovies()) {
            handleClicks(it)
        }
        recyclerMovieRecommendation.adapter = movieRecommendationAdapter

        viewModel._movieRecommendationsLiveData.observe(requireActivity(), Observer {
            it?.let {
                Log.e("Dhaval", "initMovieRecommendations: ${it}", )

                if (it.results.isNotEmpty()) {
                    recommendationLayout.visibility = View.VISIBLE
                    movieRecommendationAdapter.refreshMovieList(it)
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

                val movieId: Int = movieResult.id
                if (movieId > 0) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val movie: Movie = viewModel.getMovieDetails(movieId)
                        ShareData.data = movie

                        findNavController().navigate(R.id.action_movieFragment_self)
                        Log.e("Dhaval", "handleClicks: movie : ${movie}",)
                    }
                }
        }
        catch (e : java.lang.Exception){
            Log.e("Dhaval", "handleClicks: exception : ${e.toString()}")

            val cast = handleClicksModel.modelClass as Cast
            Log.e("Dhaval", "handleClicks: cast : ${cast.character}", )

             val id = cast.id
            ShareData.data = id
            findNavController().navigate(R.id.action_movieFragment_to_personFragment)
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