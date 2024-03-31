package com.example.weather.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.weather.R
import com.example.weather.databinding.FragmentMapBinding
import com.example.weather.location.LocationInfo
import com.example.weather.search.LocationSearchFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding!!

    private var map: GoogleMap? = null

    private val showLocationViewModel: ShowLocationViewModel by activityViewModels()

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
            displayLocation(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    private fun displayLocation(location: LocationInfo) {
        val zoom = CameraPosition.builder()
            .target(LatLng(location.lat, location.lon))
            .zoom(8f)
            .build()
        map?.moveCamera(CameraUpdateFactory.newCameraPosition(zoom))
        map?.addMarker(MarkerOptions().position(LatLng(location.lat, location.lon)))
    }
}