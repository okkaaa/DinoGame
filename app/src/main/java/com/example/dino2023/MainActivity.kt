package com.example.dino2023

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout



class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()

        hidenav(window)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenheight = displayMetrics.heightPixels
        val screenwidth = displayMetrics.widthPixels
        val layout: FrameLayout

        setContentView(R.layout.activity_main)
        val startbutton: Button = findViewById(R.id.start)
        val btbutton : Button =findViewById(R.id.bluetooth)
        layout = findViewById(R.id.mainlayout)

        startbutton.setOnClickListener {
                val intent = Intent(this@MainActivity, Select::class.java)
                startActivity(intent)
        }
        btbutton.setOnClickListener {
                val intent = Intent(this@MainActivity, Btpairing::class.java)
                startActivity(intent)

        }

        layout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hidenav(window)
            }
            true
        }
    }
}

