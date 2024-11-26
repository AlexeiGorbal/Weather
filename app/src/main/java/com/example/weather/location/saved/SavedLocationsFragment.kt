package com.example.weather.location.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentSavedLocationsBinding
import com.example.weather.location.saved.list.SavedLocationsAdapter
import com.example.weather.location.search.LocationSearchFragment
import com.example.weather.settings.SettingsFragment
import com.example.weather.utils.SimpleItemTouchCallback
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
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            .apply { setDrawable(resources.getDrawable(R.drawable.divider)) }
        binding.savedLocations.addItemDecoration(dividerItemDecoration)

        val adapter = SavedLocationsAdapter {
            val bundle = Bundle()
            bundle.putParcelable(SAVED_LOCATION_KEY, it)
            setFragmentResult(SAVED_LOCATION_REQUEST_KEY, bundle)
        }
        binding.savedLocations.adapter = adapter

        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.settings.setOnClickListener {
            childFragmentManager.commit {
                replace(R.id.child_fragment_container, SettingsFragment.newInstance())
            }
        }

        binding.searchField.setOnClickListener {
            childFragmentManager.commit {
                add(R.id.search_fragment_container, LocationSearchFragment.newInstance())
            }
        }

        val swipeCallback = object : SimpleItemTouchCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.removeLocation(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.savedLocations)

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