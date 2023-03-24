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
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom

val level1x = arrayOf(7, 8, 11, 12, 13, 14, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 8, 10, 13, 15, 14, 16, 17, 19, 20, 17, 18, 21, 17, 20, 22, 21, 23, 24, 25, 25, 25, 25, 25, 25, 29, 32, 33, 35, 36, 38, 39, 41, 42, 44, 47, 49, 51, 55, 57, 59, 60, 61, 63, 64, 67, 68, 70, 70, 70, 70, 70, 70, 70, 71, 72, 73, 74, 75, 76, 76, 74, 75, 73, 72, 71, 69, 25, 25, 25)
val level1y = arrayOf(8, 8, 8, 8, 8, 8, 8, 12, 8, 7, 6, 5, 4, 3, 2, 1, 0, 10, 12, 11, 11, 11, 12, 12, 10, 10, 7, 7, 8, 4, 3, 3, 3, 3, 3, 3, 4, 5, 6, 7, 8, 11, 11, 11, 9, 9, 7, 7, 5, 5, 3, 4, 5, 3, 4, 6, 10, 10, 10, 8, 8, 7, 7, 3, 4, 5, 6, 7, 8, 9, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 5, 9, 10, 11)

private var bton = false // if Bluetooth is on

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

        val unit = screenheight / 11

        val dinoview : ImageView = findViewById(R.id.dinoright)
        val dinoparams = dinoview.layoutParams
        dinoparams.width = unit
        dinoparams.height = unit
        dinoview.layoutParams = dinoparams

        val levelnum : Int = intent.getIntExtra("level", -1)
        var startpoint: Int = 0


        var moveright = false
        var moveleft = false
        var slightright = false
        var slightleft = false
        var magnitude = 0

        val tv2 : TextView = findViewById(R.id.textView)


        if (Bluetoothsocketholder.socket != null) {
            bton = true
        }



        val joystickbackground : ImageView = findViewById(R.id.joystickbackground)
        val jsprite : ImageView = findViewById(R.id.jsprite)
        val backimage: View = findViewById(R.id.backimage)
        val fl: FrameLayout = findViewById(R.id.layout3)




        tv2.text = screenheight.toString()

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

        val text1:TextView = findViewById(R.id.textView3)



















        stopaccidentbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                decorView.setSystemUiVisibility(uiOptions)
            }
            true
        }


        fl.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN)  {
                decorView.setSystemUiVisibility(uiOptions)
                joystickbackground.visibility = View.VISIBLE
                jsprite.visibility = View.INVISIBLE

                val diameter = joystickbackground.width

                joystickbackground.x = event.x -diameter/2
                joystickbackground.y =  event.y -diameter/2
                startpoint = event.x.toInt()


            }
            if (event.action == MotionEvent.ACTION_UP) {
                joystickbackground.visibility = View.INVISIBLE
                jsprite.visibility = View.VISIBLE

                moveleft = false
                moveright = false
                slightright = false
                slightleft = false
            }

            if (event.action == MotionEvent.ACTION_MOVE) {
                val x_chord = event.x.toInt()
                val y_chord = event.y.toInt()
                val tolerance = 55
                val slighttolerance = 25


                if (x_chord > startpoint+tolerance) {
                    moveright = true
                }
                else if (x_chord > startpoint+slighttolerance)  {
                    moveright = false
                    slightright = true
                    magnitude = (x_chord - startpoint -slighttolerance) / 2

                }
                else {
                    moveright = false
                    slightright = false
                }

                if (x_chord < startpoint-tolerance) {
                    moveleft = true
                }
                else if (x_chord < startpoint-slighttolerance) {
                    moveleft = false
                    slightleft = true
                    magnitude = (x_chord - startpoint + slighttolerance) / 2
                }
                else {
                    moveleft = false
                    slightleft = false
                }
            }
            true
        }









    }
}



