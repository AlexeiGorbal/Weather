package com.example.weather.map

import com.example.weather.location.LocationInfo
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class PinLayer(
    private val map: GoogleMap,
    onLocationClick: (LocationInfo) -> Unit
) {

    private var userMarker: Marker? = null
    private var temporaryMarker: Marker? = null
    private var savedMarkers: List<Marker?> = emptyList()

    init {
        map.setOnMarkerClickListener {
            val location = it.tag as? LocationInfo
            if (location != null) {
                onLocationClick(location)
            }
            true
        }
    }

    fun updateUserPinOnMap(location: LocationInfo) {
        userMarker?.remove()
        userMarker = map.addMarker(
            MarkerOptions().position(LatLng(location.lat, location.lon))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
        userMarker?.tag = location
    }

    fun updateTemporaryPinOnMap(location: LocationInfo) {
        temporaryMarker?.remove()
        temporaryMarker = map.addMarker(
            MarkerOptions().position(LatLng(location.lat, location.lon))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        temporaryMarker?.tag = location
    }

    fun removeTemporaryPinFromMap() {
        temporaryMarker?.remove()
    }

    fun updateSavedPinsOnMap(list: List<LocationInfo>) {
        savedMarkers.forEach { it?.remove() }
        savedMarkers = list.map(::addSavedPinOnMap)
    }

    private fun addSavedPinOnMap(location: LocationInfo): Marker? {
        val marker = map.addMarker(
            MarkerOptions().position(LatLng(location.lat, location.lon))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )
        marker?.tag = location
        return marker
    }
}