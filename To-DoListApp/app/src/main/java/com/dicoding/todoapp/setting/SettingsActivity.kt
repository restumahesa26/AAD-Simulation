package com.dicoding.todoapp.setting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.*
import com.dicoding.todoapp.R
import com.dicoding.todoapp.notification.NotificationWorker
import com.dicoding.todoapp.utils.Constants.Companion.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager

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

        workManager = WorkManager.getInstance(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
                val workManager = context?.let {
                    WorkManager.getInstance(it)
                }
                val channelName = getString(R.string.notify_channel_name)
                val data = Data.Builder()
                    .putString(NOTIFICATION_CHANNEL_ID, channelName)
                    .build()
                val periodicWorkRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 15, TimeUnit.MINUTES)
                    .setInputData(data)
                    .build()

                if(newValue == true) {
                    workManager?.enqueue(periodicWorkRequest)
                } else {
                    workManager?.cancelWorkById(periodicWorkRequest.id)
                }
                true
            }
        }
    }
}