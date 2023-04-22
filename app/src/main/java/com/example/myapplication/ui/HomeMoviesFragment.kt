package com.example.myapplication.ui


import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.ApiCollection
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.RetrofitHelper
import com.example.myapplication.data.model.HandleClicksModel
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.TrendingMovies
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.Utils
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.OnScrollStateChangedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


class HomeMoviesFragment : Fragment() {

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var recyclerTrendingMovies: RecyclerView
    private lateinit var trendingMovieAdapter: TrendingMovieAdapter

    private lateinit var recyclerPopularMovies: RecyclerView
    private lateinit var popularMovieAdapter: TrendingMovieAdapter

    private lateinit var recyclerTrailers: RecyclerView
    private lateinit var trailersAdapter: TrendingMovieAdapter
    private lateinit var latestTrailersLayout: RelativeLayout

    private lateinit var headerLayout: RelativeLayout
    private lateinit var viewBgTrailerLayout: View

    private var firstCompletelyVisibleItem: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_movies, container, false)
        headerLayout = view.findViewById(R.id.header_layout) as RelativeLayout
        val trendingLayout = view.findViewById(R.id.trending_layout) as LinearLayout
        latestTrailersLayout = view.findViewById(R.id.trailers_layout) as RelativeLayout
        val popularLayout = view.findViewById(R.id.popular_layout) as LinearLayout
        val viewBgHeaderLayout = view.findViewById(R.id.view_background_header_layout) as View
        viewBgTrailerLayout = view.findViewById(R.id.view_bg_trailer_layout) as View

        Log.e("Dhaval", "onCreateView: headerLayout height : ${headerLayout.height}")

        // to calculate the height of the headerLayout
        // when header layout completed then get the height and set to the view for background
        headerLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove the listener to avoid continuously triggering this code
                headerLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                setViewHeight(headerLayout, viewBgHeaderLayout)
            }
        })

        val apiCollection: ApiCollection =
            RetrofitHelper.getRetrofit().create(ApiCollection::class.java)
        val moviesRepository = MoviesRepository(apiCollection)
        moviesViewModel =
            ViewModelProvider(this, MoviesFactory(moviesRepository))[MoviesViewModel::class.java]

        //region Trending Movies
        initTrendingMovies(view)
        //endregion

        //region Latest Trailers
        initTrailers(view)
        //endregion

        //region Popular Movies
        initPopularMovies(view)
        //endregion

        // called in onViewCreated because for very first image to load on trailer background
        setImageToTrailersBackground()
        return view
    }

    private fun initTrendingMovies(view: View) {
        trendingMovieAdapter = TrendingMovieAdapter(TrendingMovies()) { handleClickModel ->
            handleClicks(handleClickModel)
        }
        recyclerTrendingMovies = view.findViewById(R.id.recycler_trending_movies) as RecyclerView
        recyclerTrendingMovies.setHasFixedSize(true)
        recyclerTrendingMovies.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerTrendingMovies.adapter = trendingMovieAdapter

        CoroutineScope(Dispatchers.Main).launch {

            moviesViewModel.getTrendingMovies("movie", "day")
        }

        moviesViewModel._trendingMoviesLiveData.observe(requireActivity(), Observer {

            it.type = Constant.TYPE_MOVIE
            if (it.page > 1) {
                it.also { movie ->
                    it.results.addAll(0, trendingMovieAdapter.trendingMoviesModel.results)
                }
            }
            Log.e("Dhaval", "initTrendingMovies: page : ${it.page} -- size : ${it.results.size}")
            trendingMovieAdapter.refreshMovieList(it)
        })

        recyclerTrendingMovies.setOnScrollChangeListener(object : OnScrollStateChangedListener,
            View.OnScrollChangeListener {
            override fun onStateChanged(bottomView: View, newState: Int) {

            }

            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                // get the last element index of trending movies
                val lastVisibleElement: Int =
                    (recyclerTrendingMovies.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val currentPage : Int = trendingMovieAdapter.trendingMoviesModel.page
                val totalPages : Int = trendingMovieAdapter.trendingMoviesModel.total_pages

                // if we reached to the last element then fetch more movies from next Page
                if (lastVisibleElement > 0 && lastVisibleElement == (trendingMovieAdapter.itemCount - 1) && totalPages > currentPage) {
                    moviesViewModel.getTrendingMovies(
                        "movie",
                        "day",
                        page = currentPage + 1
                    )
                }
            }

        })
    }

    private fun initTrailers(view: View) {
        trailersAdapter = TrendingMovieAdapter(TrendingMovies()) { handleClickModel ->
            handleClicks(handleClickModel)
        }
        recyclerTrailers = view.findViewById(R.id.recycler_latest_trailers) as RecyclerView
        recyclerTrailers.setHasFixedSize(true)
        recyclerTrailers.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerTrailers.adapter = trailersAdapter

        CoroutineScope(Dispatchers.Main).launch {
            moviesViewModel.getLatestTrailers("now_playing")
        }
        moviesViewModel._latestTrailerLiveData.observe(requireActivity(), Observer {
            it.type = Constant.TYPE_TRAILER

            if (it.page > 1) {
                it.also { movie ->
                    it.results.addAll(0, trailersAdapter.trendingMoviesModel.results)
                }
            }
            trailersAdapter.refreshMovieList(it)
        })

        recyclerTrailers.setOnScrollChangeListener(object : OnScrollStateChangedListener,
            View.OnScrollChangeListener {
            override fun onStateChanged(bottomView: View, newState: Int) {

            }

            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                setImageToTrailersBackground()

                // get the last element index of trending movies
                val lastVisibleElement: Int =
                    (recyclerTrailers.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val currentPage : Int = trailersAdapter.trendingMoviesModel.page
                val totalPages : Int = trailersAdapter.trendingMoviesModel.total_pages

                // if we reached to the last element then fetch more movies from next Page
                if (lastVisibleElement > 0 && lastVisibleElement == (trailersAdapter.itemCount - 1) && totalPages > currentPage) {
                    moviesViewModel.getLatestTrailers(
                        "now_playing",
                        page = currentPage + 1
                    )
                }
            }

        })
    }

    private fun initPopularMovies(view: View) {
        popularMovieAdapter = TrendingMovieAdapter(TrendingMovies()) { handleClickModel ->
            handleClicks(handleClickModel)
        }
        recyclerPopularMovies = view.findViewById(R.id.recycler_popular_movies) as RecyclerView
        recyclerPopularMovies.setHasFixedSize(true)
        recyclerPopularMovies.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerPopularMovies.adapter = popularMovieAdapter

        CoroutineScope(Dispatchers.Main).launch {
            moviesViewModel.getPopularMovies()
        }
        moviesViewModel._popularMoviesLiveData.observe(requireActivity(), Observer {
            it.type = Constant.TYPE_MOVIE

            if (it.page > 1) {
                it.also { movie ->
                    it.results.addAll(0, popularMovieAdapter.trendingMoviesModel.results)
                }
            }
            popularMovieAdapter.refreshMovieList(it)
        })

        recyclerPopularMovies.setOnScrollChangeListener(object : OnScrollStateChangedListener,
            View.OnScrollChangeListener {
            override fun onStateChanged(bottomView: View, newState: Int) {

            }

            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                // get the last element index of trending movies
                val lastVisibleElement: Int =
                    (recyclerPopularMovies.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val currentPage : Int = popularMovieAdapter.trendingMoviesModel.page
                val totalPages : Int = popularMovieAdapter.trendingMoviesModel.total_pages

                // if we reached to the last element then fetch more movies from next Page
                if (lastVisibleElement > 0 && lastVisibleElement == (popularMovieAdapter.itemCount - 1) && totalPages > currentPage) {
                    moviesViewModel.getPopularMovies(
                        page = currentPage + 1
                    )
                }
            }

        })
    }


    private fun handleClicks(handleClicksModel: HandleClicksModel): Unit {
        if (handleClicksModel.type == Constant.TYPE_TRAILER) {

            try {
                // the trailer is clicked
                if (handleClicksModel.isItemClicked) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result =
                            moviesViewModel.getLatestTrailerVideos((handleClicksModel.modelClass as MovieResult).id).results.filter {
                                it.type == "Trailer" || it.type == "Teaser"
                            }
                        val movieKey: String = result[0].key
                        movieKey.let { movieId ->
                            redirectToYoutube(movieId)
                        }
                    }
                }
                // execute else block, if the trailer's image is ready to display
                else {
                    handleClicksModel.let {
                        if (it.modelClass is MovieResult) {
                            setHeaderBgImage(it.modelClass.drawable)
                        }
                    }

                    setViewHeight(latestTrailersLayout, viewBgTrailerLayout)
                }
            } catch (e: Exception) {
                Log.e("Dhaval", "handleClicks: exception : ${e.toString()}")
            }
        }
    }

    private fun setViewHeight(layout: View, view: View) {
        val height = layout.height
        Log.e("Dhaval", "onGlobalLayout: trailer lay : ${height}")

        // Set the height of viewBgTrailerLayout to match the height of the latestTrailersLayout
        val layoutParams = view.layoutParams
        layoutParams.height = height
        view.layoutParams = layoutParams
    }

    private fun redirectToYoutube(movieId: String) {
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movieId))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.youtube");
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireActivity(), "You dont have Youtube installed", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun setImageToTrailersBackground() {
        firstCompletelyVisibleItem =
            (recyclerTrailers.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                .let {
                    if (it > 0) it else firstCompletelyVisibleItem
                }
        trailersAdapter.let {
            it.trendingMoviesModel.let {
                if (it.results.isNotEmpty()) {
                    latestTrailersLayout.background =
                        it.results[firstCompletelyVisibleItem].drawable
                }
            }
        }
    }

    private fun setHeaderBgImage(trendingMovieImg: Drawable?) {
        if (headerLayout != null && headerLayout.background == null) {
            trendingMovieImg?.let {
                headerLayout.background = trendingMovieImg
            }

            setImageToTrailersBackground()
        }
    }
}