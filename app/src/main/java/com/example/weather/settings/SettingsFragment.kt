package com.example.weather.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.weather.TemperatureUnit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.toggleButton.onToggledListener = { _, toggle, selected ->
            when (toggle.id) {
                R.id.celsius -> {
                    viewModel.changeTempUnit(TemperatureUnit.CELSIUS)
                }

                R.id.fahrenheit -> {
                    viewModel.changeTempUnit(TemperatureUnit.FAHRENHEIT)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.tempUnit.flowWithLifecycle(lifecycle).collect {
                if (it == TemperatureUnit.CELSIUS) {
                    binding.toggleButton.setToggled(R.id.celsius, true)
                } else {
                    binding.toggleButton.setToggled(R.id.fahrenheit, true)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = SettingsFragment()
    }
}