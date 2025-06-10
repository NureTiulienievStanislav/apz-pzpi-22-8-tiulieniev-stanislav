package com.trackway.app

import android.app.Application
import com.trackway.app.data.AppContainer
import com.trackway.app.data.DefaultAppContainer

class TrackWayApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
} 