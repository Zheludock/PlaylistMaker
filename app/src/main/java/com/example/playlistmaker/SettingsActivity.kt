package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<TextView>(R.id.settings_top_bar)
        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<TextView>(R.id.btn_share)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_ya_dev))
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться курсом"))
        }

        val supportButton = findViewById<TextView>(R.id.btn_support)
        supportButton.setOnClickListener {
            val email = getString(R.string.my_email)
            val subject = getString(R.string.email_subject)
            val body = getString(R.string.email_body)
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            startActivity(emailIntent)
        }

        val contractButton = findViewById<TextView>(R.id.btn_contract)
        contractButton.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_to_contract)))
            startActivity(urlIntent)
        }

        val switch = findViewById<SwitchCompat>(R.id.dark_theme_toogle)

        switch.isChecked = (application as App).isDarkTheme()

        switch.setOnCheckedChangeListener { _, checked ->
            (application as App).switchTheme(checked)
        }
    }
}