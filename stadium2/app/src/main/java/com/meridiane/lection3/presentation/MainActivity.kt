package com.meridiane.lection3.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.meridiane.lection3.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        fitContentViewToInsets()

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }


    private fun fitContentViewToInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}