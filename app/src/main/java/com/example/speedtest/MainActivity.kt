package com.example.speedtest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat


class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Оновлюємо стан швидкості
                speedState.value = location.speed.toString()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        setContent {

            val speed = mutableStateOf("Start")
            var buttonStatus = false
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // Кнопка "Start"
                Button(modifier = Modifier
                    .height(144.dp)
                    .width(144.dp), onClick = {
                    buttonStatus = !buttonStatus
                    // Стартуємо службу

                    if (buttonStatus) {
                        speed.value = speedState.value.substring(0, 3)
                        Intent(applicationContext, LocationService::class.java).apply {
                            action = LocationService.ACTION_START
                            startService(this)

                        }

                        // Підписуємося на оновлення місцезнаходження
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0L,
                            0f,
                            locationListener
                        )
                    } else {
                        speed.value = "Start"
                        // Зупиняємо службу
                        Intent(applicationContext, LocationService::class.java).apply {
                            action = LocationService.ACTION_STOP
                            startService(this)
                        }

                        // Відписуємося від оновлень місцезнаходження
                        locationManager.removeUpdates(locationListener)
                    }
                }) {
                    Text(fontSize = 32.sp,text = speed.value)
                }
            }
        }
    }

    // Стан для зберігання швидкості
    private val speedState = mutableStateOf("0.0")
}





