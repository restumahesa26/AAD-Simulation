package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // TODO 10 : Update theme based on value in ListPreference
        val prefDarkMode = findPreference<ListPreference>(getString(R.string.pref_key_dark)) as ListPreference
        prefDarkMode?.setOnPreferenceChangeListener { _, newValue ->
            val position = prefDarkMode.findIndexOfValue(newValue.toString())
            updateTheme(position)
        }

        // TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify)) as SwitchPreference
        prefNotification?.setOnPreferenceChangeListener { _, _ ->
            if (!prefNotification.isChecked) {
                DailyReminder().setDailyReminder(requireContext())
            }
            if (prefNotification.isChecked) {
                DailyReminder().cancelAlarm(requireContext())
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        val followSystem = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val on = AppCompatDelegate.MODE_NIGHT_YES
        val off = AppCompatDelegate.MODE_NIGHT_NO
        when(nightMode) {
            0 -> AppCompatDelegate.setDefaultNightMode(followSystem)
            1 -> AppCompatDelegate.setDefaultNightMode(on)
            2 -> AppCompatDelegate.setDefaultNightMode(off)
        }
        requireActivity().recreate()
        return true
    }
}