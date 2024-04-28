package com.example.weather.weather.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.weather.databinding.FragmentLocationWeatherBinding
import com.example.weather.weather.details.list.LocationWeatherAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationWeatherFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentLocationWeatherBinding? = null
    private val binding: FragmentLocationWeatherBinding
        get() = _binding!!

    private val viewModel: LocationWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationWeatherBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = LocationWeatherAdapter()
        binding.weatherRecyclerView.adapter = adapter

        val locationId = arguments?.getLong(LOCATION_ID)
        locationId?.also {
            viewModel.loadWeather(it)
        }

        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle).collect {
                if (it is UiState.DisplayWeather) {
                    val list = WeatherItemMapper(it.tempUnit).map(it.weather)
                    adapter.submitList(list)
                }
            }
        }
    }

    companion object {

        private const val LOCATION_ID = "locationId"

        fun newInstance(locationId: Long) = LocationWeatherFragment().apply {
            val bundle = Bundle()
            bundle.putLong(LOCATION_ID, locationId)
            arguments = bundle
        }
    }
}