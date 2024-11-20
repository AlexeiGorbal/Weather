package com.example.weather.location.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.databinding.FragmentLocationSearchBinding
import com.example.weather.location.search.list.LocationSearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LocationSearchFragment : Fragment() {

    private var _binding: FragmentLocationSearchBinding? = null
    private val binding: FragmentLocationSearchBinding
        get() = _binding!!

    private val viewModel: LocationSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationSearchBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            .apply { setDrawable(resources.getDrawable(R.drawable.divider)) }
        binding.listLocation.addItemDecoration(dividerItemDecoration)

        val adapter = LocationSearchAdapter {
            val bundle = Bundle()
            bundle.putParcelable(SELECTED_LOCATION_KEY, it)
            setFragmentResult(SELECTED_LOCATION_REQUEST_KEY, bundle)
        }
        binding.listLocation.adapter = adapter

        var searchLocationJob: Job? = null
        binding.searchLocation.doAfterTextChanged {
            searchLocationJob?.cancel()
            searchLocationJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(350L)
                viewModel.searchLocations(it.toString())
            }
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle).collect {
                when (it) {
                    is UiState.Initial -> {
                        adapter.submitList(emptyList())
                        binding.progressBar.isVisible = false
                        binding.noResults.isVisible = false
                    }

                    is UiState.Loading -> {
                        adapter.submitList(emptyList())
                        binding.progressBar.isVisible = true
                        binding.noResults.isVisible = false
                    }

                    is UiState.NoResults -> {
                        adapter.submitList(emptyList())
                        binding.progressBar.isVisible = false
                        binding.noResults.isVisible = true
                    }

                    is UiState.Suggestions -> {
                        adapter.submitList(it.locations)
                        binding.progressBar.isVisible = false
                        binding.noResults.isVisible = false
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showKeyboardFor(binding.searchLocation)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboardFor(binding.searchLocation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showKeyboardFor(editText: EditText) {
        editText.requestFocus()
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, 0)
    }

    private fun hideKeyboardFor(editText: EditText) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
        editText.clearFocus()
    }

    companion object {

        const val SELECTED_LOCATION_REQUEST_KEY = "selected_location_request_key"
        const val SELECTED_LOCATION_KEY = "selected_location_key"

        fun newInstance() = LocationSearchFragment()
    }
}