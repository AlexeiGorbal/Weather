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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
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
                val fragment = childFragmentManager.findFragmentById(R.id.child_fragment_container)
                if (fragment != null) {
                    childFragmentManager.commit {
                        remove(fragment)
                    }
                    return
                }

                when (bottomSheetBehavior?.state) {
                    STATE_EXPANDED -> {
                        bottomSheetBehavior?.state = STATE_COLLAPSED
                    }

                    STATE_COLLAPSED -> {
                        bottomSheetBehavior?.state = STATE_HIDDEN
                    }

                    else -> {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
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

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior?.state = STATE_HIDDEN
        bottomSheetBehavior?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_COLLAPSED) {
                    binding.saveLocation.show()
                } else {
                    binding.saveLocation.hide()
                    binding.saveLocation.isVisible = false
                }

                if (newState == STATE_HIDDEN) {
                    pinLayer?.removeTemporaryPinFromMap()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        binding.saveLocation.hide()

        binding.searchField.setOnClickListener {
            openFragment(LocationSearchFragment.newInstance())
        }

        binding.userLocation.setOnClickListener {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }

        binding.savedLocations.setOnClickListener {
            openFragment(SavedLocationsFragment.newInstance())
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

        lifecycleScope.launch {
            viewModel.selectedLocation.flowWithLifecycle(lifecycle).collect {
                centerMap(it.lat, it.lon)
                pinLayer?.updateTemporaryPinOnMap(it)
                showLocationWeatherFragment(it.id)
                bottomSheetBehavior?.state = STATE_COLLAPSED
            }
        }

        lifecycleScope.launch {
            viewModel.isLocationSaved.flowWithLifecycle(lifecycle).collect {
                if (it) {
                    binding.saveLocation.setImageResource(R.drawable.ic_bookmark_added)
                } else {
                    binding.saveLocation.setImageResource(R.drawable.ic_bookmark_add)
                }
            }
        }

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
            bottomSheetBehavior?.state = STATE_HIDDEN
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

    private fun openFragment(fragment: Fragment) {
        childFragmentManager.commit {
            replace(R.id.child_fragment_container, fragment)
            addToBackStack(null)
        }
    }

    private fun centerMap(lat: Double, lon: Double) {
        val zoom = CameraPosition.builder()
            .target(LatLng(lat, lon))
            .zoom(8f)
            .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(zoom))
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