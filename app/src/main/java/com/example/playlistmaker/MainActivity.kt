package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val findButton = findViewById<Button>(R.id.btn_search)
        val mediaButton = findViewById<Button>(R.id.btn_media)
        val setiingsButton = findViewById<Button>(R.id.btn_settings)

        findButton.setOnClickListener{
            val findIntent = Intent(this, FindActivity::class.java)
            startActivity(findIntent)
        }
        mediaButton.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }
        setiingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}