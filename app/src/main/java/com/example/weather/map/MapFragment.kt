package com.example.weather.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.weather.R
import com.example.weather.databinding.FragmentMapBinding
import com.example.weather.location.LocationInfo
import com.example.weather.search.LocationSearchFragment
import com.example.weather.weather.LocationWeather
import com.example.weather.weather.ui.LocationWeatherFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding!!

    private var map: GoogleMap? = null

    private val showLocationViewModel: ShowLocationViewModel by activityViewModels()

    private var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment_container) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN

        binding.searchField.setOnClickListener {
            childFragmentManager.commit {
                replace(
                    R.id.child_fragment_container,
                    LocationSearchFragment.newInstance()
                )
                addToBackStack(null)
            }
        }

        showLocationViewModel.selectedLocation.observe(viewLifecycleOwner) {
            childFragmentManager.popBackStack()
            centerMap(it.lat, it.lon)
            addTemporaryPinOnMap(it.lat, it.lon)
            showLocationWeatherFragment(it.id)
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    private fun centerMap(lat: Double, lon: Double) {
        val zoom = CameraPosition.builder()
            .target(LatLng(lat, lon))
            .zoom(8f)
            .build()
        map?.moveCamera(CameraUpdateFactory.newCameraPosition(zoom))
    }

    private fun addTemporaryPinOnMap(lat: Double, lon: Double) {
        map?.addMarker(MarkerOptions().position(LatLng(lat, lon)))
    }

    private fun showLocationWeatherFragment(locationId: Long) {
        childFragmentManager.commit {
            replace(
                R.id.weather_fragment_container,
                LocationWeatherFragment.newInstance(locationId)
            )
        }
    }
}