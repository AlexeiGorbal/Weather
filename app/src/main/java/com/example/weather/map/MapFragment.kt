package com.example.weather.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.databinding.FragmentMapBinding
import com.example.weather.location.LocationInfo
import com.example.weather.location.saved.SavedLocationsFragment.Companion.SAVED_LOCATION_KEY
import com.example.weather.location.saved.SavedLocationsFragment.Companion.SAVED_LOCATION_REQUEST_KEY
import com.example.weather.location.search.LocationSearchFragment.Companion.SELECTED_LOCATION_KEY
import com.example.weather.location.search.LocationSearchFragment.Companion.SELECTED_LOCATION_REQUEST_KEY
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding!!

    private var map: GoogleMap? = null
    private var pinLayer: PinLayer? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            onLocationPermissionGranted()
        }
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val viewModel: MapViewModel by viewModels()

    private var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (closeAnyChildFragment()) {
                    return
                }

//                when (bottomSheetBehavior?.state) {
//                    STATE_EXPANDED -> {
//                        bottomSheetBehavior?.state = STATE_COLLAPSED
//                    }
//
//                    STATE_COLLAPSED -> {
//                        viewModel.onLocationDeselected()
//                    }
//
//                    else -> {
//                        isEnabled = false
//                        requireActivity().onBackPressedDispatcher.onBackPressed()
//                        isEnabled = true
//                    }
//                }
                isEnabled = true
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        permissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.userLocation.setOnClickListener {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }

        childFragmentManager.setFragmentResultListener(
            SELECTED_LOCATION_REQUEST_KEY,
            this
        ) { _, bundle ->
            val location: LocationInfo? = bundle.getParcelable(SELECTED_LOCATION_KEY)
            if (location != null) {
                closeAnyChildFragment()
                viewModel.onLocationSelected(location)
            }
        }

        childFragmentManager.setFragmentResultListener(
            SAVED_LOCATION_REQUEST_KEY,
            this
        ) { _, bundle ->
            val location: LocationInfo? = bundle.getParcelable(SAVED_LOCATION_KEY)
            if (location != null) {
                closeAnyChildFragment()
                viewModel.onLocationSelected(location)
            }
        }

        lifecycleScope.launch {
            viewModel.selectedLocation.flowWithLifecycle(lifecycle).collect {
                if (it == null) {
                    pinLayer?.removeTemporaryPinFromMap()
//                    bottomSheetBehavior?.state = STATE_HIDDEN
                } else {
                    centerMap(it.lat, it.lon)
                    pinLayer?.updateTemporaryPinOnMap(it)
//                    showLocationWeatherFragment(it.id)
                    //  bottomSheetBehavior?.state = STATE_COLLAPSED
                }
            }
        }

//        lifecycleScope.launch {
//            viewModel.isLocationSaved.flowWithLifecycle(lifecycle).collect {
//                if (it) {
//                    binding.saveLocation.setImageResource(R.drawable.ic_bookmark_added)
//                } else {
//                    binding.saveLocation.setImageResource(R.drawable.ic_bookmark_add)
//                }
//            }
//        }

        lifecycleScope.launch {
            viewModel.locations.flowWithLifecycle(lifecycle).collect {
                pinLayer?.updateSavedPinsOnMap(it)
            }
        }

        lifecycleScope.launch {
            viewModel.userLocation.flowWithLifecycle(lifecycle).collect {
                pinLayer?.updateUserPinOnMap(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setOnMapClickListener {
            viewModel.onLocationDeselected()
        }

        map?.setOnMapLongClickListener { latLng ->
            viewModel.onLocationSelectedOnMap(latLng.latitude, latLng.longitude)
        }

        pinLayer = PinLayer(requireContext(), googleMap) {
            viewModel.onLocationSelected(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun onLocationPermissionGranted() {
        fusedLocationClient
            ?.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            ?.addOnSuccessListener {
                if (it != null) {
                    viewModel.onUserLocationAvailable(it.latitude, it.longitude)
                }
            }
    }

    private fun openChildFragment(fragment: Fragment) {
        childFragmentManager.commit {
            replace(R.id.child_fragment_container1, fragment)
        }
    }

    private fun closeAnyChildFragment(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.child_fragment_container1)
        return if (fragment == null) {
            false
        } else {
            childFragmentManager.commit { remove(fragment) }
            true
        }
    }

    private fun centerMap(lat: Double, lon: Double) {
        val zoom = CameraPosition.builder()
            .target(LatLng(lat, lon))
            .zoom(8f)
            .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(zoom))
    }

//    private fun showLocationWeatherFragment(locationId: Long) {
//        childFragmentManager.commit {
//            replace(
//                R.id.weather_fragment_container,
//                LocationWeatherFragment.newInstance(locationId)
//            )
//        }
//    }

    companion object {

        fun newInstance() = MapFragment()
    }
}