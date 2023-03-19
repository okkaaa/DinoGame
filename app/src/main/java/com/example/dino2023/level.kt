package com.example.dino2023

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

val level1x = arrayOf(7, 8, 11, 12, 13, 14, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 8, 10, 13, 15, 14, 16, 17, 19, 20, 17, 18, 21, 17, 20, 22, 21, 23, 24, 25, 25, 25, 25, 25, 25, 29, 32, 33, 35, 36, 38, 39, 41, 42, 44, 47, 49, 51, 55, 57, 59, 60, 61, 63, 64, 67, 68, 70, 70, 70, 70, 70, 70, 70, 71, 72, 73, 74, 75, 76, 76, 74, 75, 73, 72, 71, 69, 25, 25, 25)
val level1y = arrayOf(8, 8, 8, 8, 8, 8, 8, 12, 8, 7, 6, 5, 4, 3, 2, 1, 0, 10, 12, 11, 11, 11, 12, 12, 10, 10, 7, 7, 8, 4, 3, 3, 3, 3, 3, 3, 4, 5, 6, 7, 8, 11, 11, 11, 9, 9, 7, 7, 5, 5, 3, 4, 5, 3, 4, 6, 10, 10, 10, 8, 8, 7, 7, 3, 4, 5, 6, 7, 8, 9, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 5, 9, 10, 11)

class Level : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_layout)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()

        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.setSystemUiVisibility(uiOptions)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenheight = displayMetrics.heightPixels
        val screenwidth = displayMetrics.widthPixels
        var layout: FrameLayout
        val temp : String? = intent.getStringExtra("levelnumber")
        val levelnum = temp?.toInt()


        val jumpbutton: Button = findViewById(R.id.jumpbutton)
        val stopaccidentbutton: Button = findViewById(R.id.stopaccidentbutton)
        val restartbutton: Button = findViewById(R.id.restart)
        val backbutton: Button = findViewById(R.id.back1)


        backbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish()
            }
            true
        }

        //val text1:TextView = findViewById(R.id.textView3)






        //finish()
        //return




    }
}



