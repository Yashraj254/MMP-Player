package com.example.mmpplayer

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mmpplayer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (!checkPermission()) {
            requestPermission()
            return
        }

    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
        )
            Toast.makeText(this,
                "READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS",
                Toast.LENGTH_SHORT).show()
        else
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123)
    }

    override fun onStop() {
        super.onStop()
        Log.i("Fragments", "onStop: Main Activity")
    }
}