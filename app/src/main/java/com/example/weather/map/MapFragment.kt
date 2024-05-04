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
import com.example.weather.location.saved.SavedLocationsFragment
import com.example.weather.location.saved.SavedLocationsFragment.Companion.SAVED_LOCATION_KEY
import com.example.weather.location.saved.SavedLocationsFragment.Companion.SAVED_LOCATION_REQUEST_KEY
import com.example.weather.location.search.LocationSearchFragment
import com.example.weather.location.search.LocationSearchFragment.Companion.SELECTED_LOCATION_KEY
import com.example.weather.location.search.LocationSearchFragment.Companion.SELECTED_LOCATION_REQUEST_KEY
import com.example.weather.settings.SettingsFragment
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
import kotlin.math.max
import kotlin.math.min

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

                when (bottomSheetBehavior?.state) {
                    STATE_EXPANDED -> {
                        bottomSheetBehavior?.state = STATE_COLLAPSED
                    }

                    STATE_COLLAPSED -> {
                        viewModel.onLocationDeselected()
                    }

                    else -> {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        isEnabled = true
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

        val bottomSheetPeekHeight =
            resources.getDimensionPixelSize(R.dimen.bottom_sheet_peek_height)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior?.state = STATE_HIDDEN
        bottomSheetBehavior?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_HIDDEN) {
                    viewModel.onLocationDeselected()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val saveLocationHalfHeight = binding.saveLocation.height / 2
                if (slideOffset > 0) {
                    binding.saveLocation.alpha = 1 - min(1f, slideOffset * 2)

                    binding.saveLocation.translationY =
                        -(bottomSheetPeekHeight + slideOffset * (bottomSheet.height - bottomSheetPeekHeight)) + saveLocationHalfHeight
                } else {
                    binding.saveLocation.alpha = 1 + max(-1f, slideOffset * 2)

                    val progress = 1 + slideOffset
                    binding.saveLocation.translationY =
                        -(progress * bottomSheetPeekHeight) + saveLocationHalfHeight
                }

                val progress = 1 + min(0f, slideOffset)
                binding.userLocation.translationY =
                    -(progress * (bottomSheetPeekHeight + saveLocationHalfHeight))
            }
        })
        binding.saveLocation.alpha = 0f

        binding.searchField.setOnClickListener {
            openChildFragment(LocationSearchFragment.newInstance())
        }

        binding.settings.setOnClickListener {
            openChildFragment(SettingsFragment.newInstance())
        }

        binding.userLocation.setOnClickListener {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }

        binding.savedLocations.setOnClickListener {
            openChildFragment(SavedLocationsFragment.newInstance())
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
                    bottomSheetBehavior?.state = STATE_HIDDEN
                } else {
                    centerMap(it.lat, it.lon)
                    pinLayer?.updateTemporaryPinOnMap(it)
                    showLocationWeatherFragment(it.id)
                    bottomSheetBehavior?.state = STATE_COLLAPSED
                }
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
            replace(R.id.child_fragment_container, fragment)
        }
    }

    private fun closeAnyChildFragment(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.child_fragment_container)
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

    private fun showLocationWeatherFragment(locationId: Long) {
        childFragmentManager.commit {
            replace(
                R.id.weather_fragment_container,
                LocationWeatherFragment.newInstance(locationId)
            )
        }
    }
}
