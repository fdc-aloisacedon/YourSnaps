package com.example.yoursnaps.framework

import android.content.Context
import androidx.core.content.edit

class PreferencesManager(context: Context) {

    companion object {
        private const val PREF_NAME = "UserPermissionPrefs"
    }
    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLocationPermission(isGranted: String) {
        sharedPreferences.edit { putString("location", isGranted) }
    }

    fun getLocationPermission(): String? {
        return sharedPreferences.getString("location", null)
    }

    fun clearAllPreferences() {
        sharedPreferences.edit { clear() }
    }
}