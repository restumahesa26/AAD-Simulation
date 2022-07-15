package com.dicoding.courseschedule.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.*
import java.util.*

class DailyReminder : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        executeThread {
            val repository = DataRepository.getInstance(context)
            val courses = repository?.getTodaySchedule()

            courses?.let {
                if (it.isNotEmpty()) showNotification(context, it)
            }
        }
    }

    // TODO 12 : Implement daily reminder for every 06.00 a.m using AlarmManager
    @RequiresApi(Build.VERSION_CODES.M)
    fun setDailyReminder(context: Context) {
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, Intent(context, DailyReminder::class.java), PendingIntent.FLAG_IMMUTABLE)
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, "Success Set Daily Reminder", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun cancelAlarm(context: Context) {
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,ID_REPEATING, intent, PendingIntent.FLAG_IMMUTABLE)
        pendingIntent.cancel()
        alarm.cancel(pendingIntent)
        Toast.makeText(context, "Success Cancel Daily Reminder", Toast.LENGTH_SHORT).show()
    }

    private fun showNotification(context: Context, content: List<Course>) {
        // TODO 13 : Show today schedules in inbox style notification & open HomeActivity when notification tapped
        val notifStyle = NotificationCompat.InboxStyle()
        val timeFormat = context.resources.getString(R.string.notification_message_format)
        content.forEach {
            val courseData = String.format(timeFormat, it.startTime, it.endTime, it.courseName)
            notifStyle.addLine(courseData)
        }

        val getPendingIntent = pendingIntent(context)
        val notifCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.resources.getString(R.string.today_schedule))
            .setSmallIcon(R.drawable.ic_notifications)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setStyle(notifStyle)
            .setSound(notifSound)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(getPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notifChannel.enableVibration(true)
            notifChannel.vibrationPattern = longArrayOf(1000, 1000, 1000)
            notifBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notifCompat.createNotificationChannel(notifChannel)
        }

        val notifBuild = notifBuilder.build()
        notifCompat.notify(NOTIFICATION_ID, notifBuild)
    }

    private fun pendingIntent(context: Context): PendingIntent? {
        val intent = Intent(context, HomeActivity::class.java)
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}