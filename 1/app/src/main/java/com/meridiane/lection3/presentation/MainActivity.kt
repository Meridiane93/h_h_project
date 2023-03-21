package com.meridiane.lection3.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.meridiane.lection3.R
import com.meridiane.lection3.presentation.ui.signin.SignInFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)


        setContentView(R.layout.activity_main)

        fitContentViewToInsets()

        supportFragmentManager.commit{
            add<SignInFragment>(R.id.container)
        }
    }

    private fun fitContentViewToInsets() {
       WindowCompat.setDecorFitsSystemWindows(window,false)
    }
}