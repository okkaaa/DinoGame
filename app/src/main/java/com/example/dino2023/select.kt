package com.example.dino2023

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class Select : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()

        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.setSystemUiVisibility(uiOptions)

        setContentView(R.layout.levelselect)
        val layout : FrameLayout = findViewById(R.id.mainlayout1)
        val backbutton: Button = findViewById(R.id.back)
        val button1: Button = findViewById(R.id.level1)
        val button2: Button = findViewById(R.id.level2)
        val button3: Button = findViewById(R.id.level3)
        val button4: Button = findViewById(R.id.level4)
        val button5: Button = findViewById(R.id.level5)
        val button6: Button = findViewById(R.id.level6)


        backbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish()
            }
            true
        }
        button1.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val intent = Intent(this@Select, Level::class.java)
                intent.putExtra("levelnumber", "1")
                startActivity(intent)
            }
            true
        }
        layout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                decorView.setSystemUiVisibility(uiOptions)
            }
            true
        }
    }
}