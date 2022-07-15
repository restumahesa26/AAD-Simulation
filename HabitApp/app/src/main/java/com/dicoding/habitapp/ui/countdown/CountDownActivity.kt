package com.dicoding.habitapp.ui.countdown

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.*
import java.util.*
import java.util.concurrent.TimeUnit

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
        viewModel.currentTimeString.observe(this) { time ->
            findViewById<TextView>(R.id.tv_count_down).text = time
        }

        viewModel.setInitialTime(habit.minutesFocus)

        viewModel.eventCountDownFinish.observe(this) { isFinish ->
            if (isFinish) updateButtonState(false)
        }

        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
        val workManager = WorkManager.getInstance(this)

        val data = Data.Builder()
            .putInt(HABIT_ID, habit.id)
            .putString(HABIT_TITLE, habit.title)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(data)
            .setInitialDelay(habit.minutesFocus, TimeUnit.MINUTES)
            .build()

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            updateButtonState(true)
            workManager.enqueueUniqueWork(NOTIF_UNIQUE_WORK, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)
            viewModel.startTimer()
            Log.d("CountDownActivity", "Count Down Started")
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            updateButtonState(false)
            workManager.cancelUniqueWork(NOTIF_UNIQUE_WORK)
            viewModel.resetTimer()
            Log.d("CountDownActivity", "Count Down Stopped")
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}