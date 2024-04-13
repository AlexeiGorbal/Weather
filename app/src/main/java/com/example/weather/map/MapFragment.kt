package com.example.weather.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.databinding.FragmentMapBinding
import com.example.weather.location.LocationInfo
import com.example.weather.location.saved.SavedLocationsFragment
import com.example.weather.location.saved.SavedLocationsFragment.Companion.SAVED_LOCATION_KEY
import com.example.weather.location.saved.SavedLocationsFragment.Companion.SAVED_LOCATION_REQUEST_KEY
import com.example.weather.location.search.LocationSearchFragment
import com.example.weather.location.search.LocationSearchFragment.Companion.SELECTED_LOCATION_KEY
import com.example.weather.location.search.LocationSearchFragment.Companion.SELECTED_LOCATION_REQUEST_KEY
import com.example.weather.weather.details.LocationWeatherFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding!!

    private var map: GoogleMap? = null

    private val viewModel: MapViewModel by viewModels()

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
        bottomSheetBehavior?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_COLLAPSED) {
                    binding.saveLocation.show()
                } else {
                    binding.saveLocation.hide()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        binding.saveLocation.hide()

        binding.searchField.setOnClickListener {
            openingFragment(LocationSearchFragment.newInstance())
        }

        binding.savedLocations.setOnClickListener {
            openingFragment(SavedLocationsFragment.newInstance())
        }

        binding.saveLocation.setOnClickListener {
            viewModel.onSelectedLocationSavedStateChanged()
        }

        childFragmentManager.setFragmentResultListener(
            SELECTED_LOCATION_REQUEST_KEY,
            this
        ) { _, bundle ->
            val location: LocationInfo? = bundle.getParcelable(SELECTED_LOCATION_KEY)
            if (location != null) {
                childFragmentManager.popBackStack()
                viewModel.onLocationSelected(location)
            }
        }

        childFragmentManager.setFragmentResultListener(
            SAVED_LOCATION_REQUEST_KEY,
            this
        ) { _, bundle ->
            val location: LocationInfo? = bundle.getParcelable(SAVED_LOCATION_KEY)
            if (location != null) {
                childFragmentManager.popBackStack()
                viewModel.onLocationSelected(location)
            }
        }

        viewModel.selectedLocation.observe(viewLifecycleOwner) { locationInfoItem ->
            centerMap(locationInfoItem.lat, locationInfoItem.lon)
            addTemporaryPinOnMap(locationInfoItem.lat, locationInfoItem.lon)
            showLocationWeatherFragment(locationInfoItem.id)
            bottomSheetBehavior?.state = STATE_COLLAPSED
        }

        viewModel.isLocationSaved.observe(viewLifecycleOwner) {
            if (it) {
                binding.saveLocation.setImageResource(R.drawable.ic_bookmark_added)
            } else {
                binding.saveLocation.setImageResource(R.drawable.ic_bookmark_add)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    private fun openingFragment(fragment: Fragment) {
        childFragmentManager.commit {
            replace(
                R.id.child_fragment_container,
                fragment
            )
            addToBackStack(null)
        }
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