package com.example.myapplication.ui.person

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.ApiCollection
import com.example.myapplication.data.MoviesRepository
import com.example.myapplication.data.RetrofitHelper
import com.example.myapplication.data.model.Cast
import com.example.myapplication.data.model.HandleClicksModel
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieResult
import com.example.myapplication.data.model.person.PersonMedia
import com.example.myapplication.data.model.tv.TvDetails
import com.example.myapplication.ui.MoviesViewModel
import com.example.myapplication.ui.home.MoviesFactory
import com.example.myapplication.utils.ShareData
import com.example.myapplication.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PersonFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var recyclerPersonCareerList : RecyclerView
    private lateinit var personCareerListAdapter: PersonCareerListAdapter
    private var personId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ShareData.data != null){
           personId = ShareData.data as Int
        }

        Log.e("Dhaval", "onCreate: personFragment : ${personId}")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_person, container, false)

        Log.e("Dhaval", "onCreateView: PersonFragment", )

        val imgPerson : ImageView = view.findViewById(R.id.img_person)
        val txtPersonName : TextView = view.findViewById(R.id.txt_person_name)
        val txtBiography : TextView = view.findViewById(R.id.txt_person_biography)
        val txtKnownFor : TextView = view.findViewById(R.id.txt_known_for)
        val txtGender : TextView = view.findViewById(R.id.txt_gender)
        val txtBirthday : TextView = view.findViewById(R.id.txt_birthday)
        val txtBirthPlace : TextView = view.findViewById(R.id.txt_pob)

        val apiCollection = RetrofitHelper.getRetrofit().create(ApiCollection::class.java)
        val repository = MoviesRepository(apiCollection)
        viewModel = ViewModelProvider(requireActivity(), MoviesFactory(repository))[MoviesViewModel::class.java]

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getPersonDetails(personId!!)
        }

        viewModel._personDetailsLiveData.observe(requireActivity(), Observer {
            it?.let {
                try {
                    Log.e("Dhaval", "onCreateView: personDetails : ${it}",)
                    // checked is fragment is added or not
                    // otherwise it will crash the  app
                    if (isAdded) {
                        val width = Utils.getScreenWidth() / 2 - 200
                        val height = (width * 1.75).toInt()

                        val params = LinearLayout.LayoutParams(width, height)
                        imgPerson.layoutParams = params

                        Glide.with(requireActivity())
                            .load(Utils.appendImgPathToUrl(it.profile_path))
                            .error(requireActivity().getDrawable(R.drawable.ic_img_not_available))
                            .into(imgPerson)

                        txtPersonName.text = it.name
                        txtBiography.text = it.biography
                        txtKnownFor.text = it.known_for_department
                        txtGender.text = if (it.gender == 2) "Male" else "Female"
                        txtBirthday.text = it.birthday.let {
                            if (it != null && it.isNotEmpty()) {
                                Utils.convertDate(it, "dd/MM/YYYY")
                            } else {
                                "-"
                            }
                        }

                        txtBirthPlace.text = it.place_of_birth
                    }
                }
                catch (e : java.lang.Exception){

                }
            }
        })

        initPersonCareer(view)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getPersonMediaList(personId!!)
        }

        viewModel._personMediaMediaListLiveData.observe(requireActivity(), Observer {

            Log.e("Dhaval", "onCreateView: personMedia : ${it}", )
            if (isAdded){
                personCareerListAdapter.refreshList(it)
            }
        })
        return view
    }

    private fun initPersonCareer(view: View) {
        recyclerPersonCareerList = view.findViewById(R.id.recycler_person_career)
        recyclerPersonCareerList.setHasFixedSize(true)

        personCareerListAdapter = PersonCareerListAdapter(PersonMedia()){
            handleClicks(it)
        }

        recyclerPersonCareerList.adapter = personCareerListAdapter
    }

    private fun handleClicks(movieResult: MovieResult){
        Log.e("Dhaval", "handleClicks: ${movieResult}", )

        try{

            val movieId: Int = movieResult.id
            if (movieId > 0) {
                CoroutineScope(Dispatchers.Main).launch {

                    if (movieResult.media_type == "movie") {
                        val movie: Movie = viewModel.getMovieDetails(movieId)
                        ShareData.data = movie

                        findNavController().navigate(R.id.action_personFragment_to_movieFragment)
                        Log.e("Dhaval", "handleClicks: movie : ${movie}",)
                    }
                    else if(movieResult.media_type == "tv"){
                        val tvSeries: TvDetails = viewModel.getTVDetails(movieId)
                        ShareData.data = tvSeries

                        findNavController().navigate(R.id.action_personFragment_to_tvSeriesFragment)
                        Log.e("Dhaval", "handleClicks: tv : ${tvSeries}",)
                    }
                }
            }
        }
        catch (e : java.lang.Exception){

        }
    }

}