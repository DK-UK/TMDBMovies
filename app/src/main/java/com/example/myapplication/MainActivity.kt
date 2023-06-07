package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.ui.home.HomeMoviesFragment
import com.example.myapplication.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var singleBack: Boolean = false

    private lateinit var layoutConnectivity : LinearLayout
    private lateinit var fragmentContainer : FragmentContainerView
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutConnectivity = findViewById(R.id.layout_connectivity) as LinearLayout
        fragmentContainer = findViewById(R.id.home_fragment)
        val btnRetry = findViewById(R.id.btn_retry) as Button

        btnRetry.setOnClickListener {
            showHomeFragmentOrInternetConnectionDialog()
        }

        showHomeFragmentOrInternetConnectionDialog()

    }

    private fun showHomeFragmentOrInternetConnectionDialog(){
        if (Utils.isInternetAvailable(this@MainActivity)) {

            fragmentContainer.visibility = View.VISIBLE
            layoutConnectivity.visibility = View.GONE

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.home_fragment) as NavHostFragment
            navController = navHostFragment.navController
            navController.setGraph(R.navigation.nav_graph)

            // Add an OnBackPressedCallback to the activity's OnBackPressedDispatcher.
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentDestination = navController.currentDestination

                    // If the current destination is not the home fragment, pop the back stack.
                    if (currentDestination?.id != R.id.home_fragment) {
                        navController.popBackStack()
                    }
                }

            })
        }
        else{
            layoutConnectivity.visibility = View.VISIBLE
            fragmentContainer.visibility = View.GONE
        }
    }

}
