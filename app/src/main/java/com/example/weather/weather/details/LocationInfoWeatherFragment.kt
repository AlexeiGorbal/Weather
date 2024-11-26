package com.example.weather.weather.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.databinding.FragmentLocationInfoWeatherBinding
import com.example.weather.location.saved.SavedLocationsFragment
import com.example.weather.map.MapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationInfoWeatherFragment : Fragment() {

    private var _binding: FragmentLocationInfoWeatherBinding? = null
    private val binding: FragmentLocationInfoWeatherBinding
        get() = _binding!!

    private val viewModel: LocationInfoWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationInfoWeatherBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = LocationInfoWeatherAdapter(this)
        lifecycleScope.launch {
            viewModel.locations.flowWithLifecycle(lifecycle).collect {
                adapter.addList(it)
            }
        }
        binding.viewPager.adapter = adapter

        binding.map.setOnClickListener {
            childFragmentManager.commit {
                replace(
                    R.id.child_fragment_container,
                    MapFragment.newInstance()
                )

            }
        }

        binding.savedLocationButton.setOnClickListener {
            childFragmentManager.commit {
                replace(
                    R.id.child_fragment_container,
                    SavedLocationsFragment.newInstance()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = LocationInfoWeatherFragment()
    }
}