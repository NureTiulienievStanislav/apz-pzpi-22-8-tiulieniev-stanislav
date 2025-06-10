package com.trackway.app.data

import com.trackway.app.data.api.TrackWayApi
import com.trackway.app.data.repository.TrackWayRepository

interface AppContainer {
    val api: TrackWayApi
    val repository: TrackWayRepository
} 