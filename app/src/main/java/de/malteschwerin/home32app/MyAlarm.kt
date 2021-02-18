package de.malteschwerin.home32app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URL

class MyAlarm : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.d("Alarm Bell", "Alarm just fired")

        val baseUrl = "http://192.168.2.167"

        val brightness: Int = intent.getExtras()?.getInt("brightness")?: 255
        val color: Int = intent.getExtras()?.getInt("color")?: 127
        val durationMinutes: Int = intent.getExtras()?.getInt("duration")?: 1
        val steps = durationMinutes * 30

        val stepTime: Long = ((durationMinutes.toFloat()/steps) * 1000 * 60).toLong()
        Log.d("Alarm Bell", "brightness: $brightness, color: $color, duration: $durationMinutes, step time: $stepTime")

        GlobalScope.launch {
            Log.d("Alarm Bell", "entered coroutine")
            for (i in 1..steps) {
                Log.d("Alarm Bell", "step $i")
                val newBrightness: Int = (brightness * (i.toFloat() / steps.toFloat())).toInt()

                val url = "$baseUrl/ambient?ambientBrightness=$newBrightness&ambientTemperature=$color"

                Log.d("Alarm Bell", url)
                val response = try {
                    URL(url)
                        .openStream()
                        .bufferedReader()
                        .use { it.readText() }

                }
                catch (e: Exception) {
                    Log.e("Alarm Bell", e.toString())
                }
                Log.d("Alarm Bell", response.toString())
                delay(stepTime)
            }
        }
    }
}