package com.example.weather.location.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.weather.databinding.FragmentSavedLocationsBinding
import com.example.weather.location.saved.list.SavedLocationsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedLocationsFragment : Fragment() {

    private var _binding: FragmentSavedLocationsBinding? = null
    private val binding: FragmentSavedLocationsBinding
        get() = _binding!!

    private val viewModel: SavedLocationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedLocationsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = SavedLocationsAdapter {
            val bundle = Bundle()
            bundle.putParcelable(SAVED_LOCATION_KEY, it)
            setFragmentResult(SAVED_LOCATION_REQUEST_KEY, bundle)
        }
        binding.savedLocations.adapter = adapter

        lifecycleScope.launch {
            viewModel.locations.flowWithLifecycle(lifecycle).collect {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val SAVED_LOCATION_REQUEST_KEY = "saved_location_request_key"
        const val SAVED_LOCATION_KEY = "saved_location_key"

        fun newInstance() = SavedLocationsFragment()
    }
}