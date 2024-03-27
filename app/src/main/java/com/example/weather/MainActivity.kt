package com.example.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.map.MapFragment
import com.example.weather.search.LocationSearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.setFragmentResultListener(
            MapFragment.SEARCH_LOCATION_REQUEST_KEY,
            this
        ) { _, _ ->
            supportFragmentManager.commit {
                add(R.id.container, LocationSearchFragment.newInstance())
                addToBackStack(null)
            }
        }
    }
}
