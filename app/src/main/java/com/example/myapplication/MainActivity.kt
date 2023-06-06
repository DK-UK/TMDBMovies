package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.ui.home.HomeMoviesFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Add an OnBackPressedCallback to the activity's OnBackPressedDispatcher.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestination = navController.currentDestination

                CoroutineScope(Dispatchers.Main).launch {
                    navController.backQueue.forEach {
                        Log.e("Dhaval", "BACK STACK : ${it.destination.displayName}", )
                    }
                }
                Log.e("Dhaval", "handleOnBackPressed: current dest : ${currentDestination?.displayName}", )

                // If the current destination is not the home fragment, pop the back stack.
                if (currentDestination?.id != R.id.home_fragment) {
                    navController.popBackStack()
                }
            }

        })
    }
}
