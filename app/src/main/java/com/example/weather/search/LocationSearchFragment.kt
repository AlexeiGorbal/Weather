package com.example.weather.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.FragmentLocationSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSearchFragment : Fragment() {

    private var _binding: FragmentLocationSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LocationSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationSearchBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.listLocation.layoutManager = LinearLayoutManager(context)
        val adapter = LocationAdapter()

        binding.searchLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.searchLocations(s.toString())
            }
        })

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Initial -> {
                    binding.progressBar.visibility = View.GONE
                    binding.textResult.visibility = View.GONE
                }

                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.textResult.visibility = View.GONE
                }

                is UiState.NoResults -> {
                    binding.progressBar.visibility = View.GONE
                    binding.textResult.visibility = View.VISIBLE
                }

                is UiState.Suggestions -> {
                    adapter.addLocations(it.locations)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
        binding.listLocation.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() {}
    }
}