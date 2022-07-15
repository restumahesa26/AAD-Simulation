package com.dicoding.habitapp.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.dicoding.habitapp.R
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            //TODO 11 : Update theme based on value in ListPreference
            val prefDarkMode = findPreference<ListPreference>(getString(R.string.pref_key_dark)) as ListPreference
            prefDarkMode?.setOnPreferenceChangeListener { _, newValue ->
                val position = prefDarkMode.findIndexOfValue(newValue.toString())
                updateTheme(position)
            }
        }

        private fun updateTheme(mode: Int): Boolean {
            val followSystem = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            val on = AppCompatDelegate.MODE_NIGHT_YES
            val off = AppCompatDelegate.MODE_NIGHT_NO
            when(mode) {
                0 -> AppCompatDelegate.setDefaultNightMode(followSystem)
                1 -> AppCompatDelegate.setDefaultNightMode(on)
                2 -> AppCompatDelegate.setDefaultNightMode(off)
            }
            requireActivity().recreate()
            return true
        }
    }
}