package de.malteschwerin.home32app

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.net.URL
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {
    private lateinit var timePicker: TimePicker
    private lateinit var btnSetAlarm: Button
    private lateinit var brightnessText: TextView
    private lateinit var colorText: TextView
    private lateinit var durationText: TextView
    private lateinit var brightnessBar: SeekBar
    private lateinit var colorBar: SeekBar
    private lateinit var durationBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "KotlinApp"

        timePicker = findViewById(R.id.timePicker)
        btnSetAlarm = findViewById(R.id.buttonAlarm)

        brightnessText = findViewById(R.id.brightnessText)
        colorText = findViewById(R.id.colorText)
        durationText = findViewById(R.id.durationText)

        brightnessBar = findViewById(R.id.brightness_bar)
        colorBar = findViewById(R.id.color_bar)
        durationBar = findViewById(R.id.duration_bar)

        btnSetAlarm.setOnClickListener {
            val brightness:Int = brightnessBar.progress
            val color:Int = colorBar.progress
            val duration: Int = durationBar.progress

            val calendar: Calendar = Calendar.getInstance()
            if (Build.VERSION.SDK_INT >= 23) {
                calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.hour,
                    timePicker.minute,
                    0
                )
            } else {
                calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.currentHour,
                    timePicker.currentMinute, 0
                )
            }
            setAlarm(calendar.timeInMillis, brightness, color, duration)
        }

        brightnessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percentage = (progress.toFloat() / 255.toFloat() * 100).roundToInt()
                brightnessText.text = "Brightness: ${percentage}%"
            }
        })

        colorBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percentage = (progress.toFloat() / 255.toFloat() * 100).roundToInt()
                colorText.text = "Color: ${percentage}%"
            }
        })

        durationBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                durationText.text = "Duration: ${progress}min"
            }
        })
    }
    private fun setAlarm(timeInMillis: Long, brightness: Int, color: Int, duration: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarm::class.java).apply {
            putExtra("brightness", brightness)
            putExtra("color", color)
            putExtra("duration", duration)
        }

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val info = AlarmManager.AlarmClockInfo(timeInMillis, pendingIntent)
        alarmManager.setAlarmClock(
            info,
            pendingIntent
        )

        Log.d("Alarm Bell", "Alarm is set")
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()
    }

}
