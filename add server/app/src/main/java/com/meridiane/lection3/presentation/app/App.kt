package com.meridiane.lection3.presentation.app

import android.app.Application
import com.meridiane.lection3.Constants
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(Constants.KEY_MAP)
        MapKitFactory.initialize(this)
    }
}