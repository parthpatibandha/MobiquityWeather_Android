package org.android.application.presentation.core.location

import android.location.Location

interface MyLocationListener {
    fun onLocationReceived(location : Location)
    fun onLocationError()
}