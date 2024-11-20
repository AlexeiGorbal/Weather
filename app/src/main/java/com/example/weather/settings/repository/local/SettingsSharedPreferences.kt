package com.example.weather.settings.repository.local

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsSharedPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var tempUnitId: Int
        set(value) = prefs.edit { putInt(TEMP_UNIT_KEY, value) }
        get() = prefs.getInt(TEMP_UNIT_KEY, 0)

    companion object {

        private const val TEMP_UNIT_KEY = "temp_unit"
    }
}