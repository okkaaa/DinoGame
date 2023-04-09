package com.example.dino2023

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import com.example.dino2023.BTMSG.Companion.data
import kotlinx.coroutines.*
import java.io.*
import java.nio.ByteBuffer


private var bton = false // if Bluetooth is on
private var friendx: Int = 0
private var friendy: Int = 0
private var friendorientright = true
private var mydinoorientright = true



class Level : AppCompatActivity() {


    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_layout)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()

        hidenav(window)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val Levelscope = CoroutineScope(Dispatchers.Main)



        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val screenwidth: Int
        val screenheight: Int

        if (Build.VERSION_CODES.R <= android.os.Build.VERSION.SDK_INT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            val layoutAttr = window.attributes
            layoutAttr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = layoutAttr

            val display = windowManager.currentWindowMetrics
            screenheight = display.bounds.height()
            screenwidth = display.bounds.width()


        }
        else {
            screenheight = displayMetrics.heightPixels
            screenwidth = displayMetrics.widthPixels
        }


        val unit = screenwidth / 24



        val restartbutton: Button = findViewById(R.id.restart)
        val jumpbutton: Button = findViewById(R.id.jumpbutton)
        val stopaccidentbutton: Button = findViewById(R.id.stopaccidentbutton)
        val backbutton: Button = findViewById(R.id.back1)
        val fl: FrameLayout = findViewById(R.id.layout3)


        val dinoview : ImageView = findViewById(R.id.dinoright)
        val dinoparams = dinoview.layoutParams
        dinoparams.width = unit
        dinoparams.height = unit

        val dinofriendview : ImageView = findViewById(R.id.dinopurple)
        dinofriendview.layoutParams = dinoparams


        dinoview.x = (11.5 * unit).toFloat()
        dinoview.y = (5 * unit).toFloat()
        dinoview.layoutParams = dinoparams

        val levelnum : Int = intent.getIntExtra("level", 1)


        val colour = "#c2b280"
        var moveright = false
        var moveleft = false
        var slightright = false
        var slightleft = false
        var move = false
        var fall = true
        var jump = false
        var canjump = true


        var magnitude = 0
        var timefallen = 1
        var jumptime = 0
        var startpoint: Int = 0
        var location = 0
        var height = 0

        val tolerance = 55
        val slighttolerance = 23
        val maxspeed = 16
        //tolerance = slighttolerance + 2*maxspeed (condition for joystick deadzone)

        val tv2 : TextView = findViewById(R.id.textView)

        if (Bluetoothsocketholder.socket != null) {
            bton = true
        }


        val joystickbackground : ImageView = findViewById(R.id.joystickbackground)
        val jsprite : ImageView = findViewById(R.id.jsprite)
        val backimage: View = findViewById(R.id.backimage)


        val platformlistx = getlayoutx(levelnum)
        val platformlisty = getlayouty(levelnum)
        val indexes = platformlistx.size


        val uptimer : CountDownTimer = object : CountDownTimer(Long.MAX_VALUE, 15) {
            override fun onTick(l: Long){
                val bitmap: Bitmap = Bitmap.createBitmap(screenwidth, screenheight, Bitmap.Config.ARGB_8888)
                val canvas: Canvas = Canvas(bitmap)
                backimage.background = BitmapDrawable(getResources(), bitmap)
                val rectlist : MutableList<ShapeDrawable> = ArrayList()
                val previousheight = height
                val previouslocation = location
                if (bton) {
                    dinofriendview.x = (-friendx + location + 11.5*unit).toFloat()
                    dinofriendview.y = (-friendy + height + 5*unit).toFloat()
                    if (friendorientright){
                        dinofriendview.rotationY = 0F
                    }
                    else {
                        dinofriendview.rotationY = 180F
                    }
                }


                if (jump) {
                    jumptime++
                    height += (40/jumptime+2) +20
                    if (jumptime > 8){
                        jump = false
                    }

                }
                for (i in 0 until indexes) {
                    val left = platformlistx[i] * unit + previouslocation
                    val top = platformlisty[i] * unit + previousheight
                    val right = left + unit
                    val bottom = top + unit
                    var toptest = platformlisty[i] * unit + height
                    val lefttest = platformlistx[i] * unit + location
                    var shapeDrawable: ShapeDrawable
                    shapeDrawable = ShapeDrawable(RectShape())




                    //topcollision
                    if (lefttest<12.3*unit && lefttest>10.7*unit && toptest > 3.99 *unit && toptest < 5*unit) {
                        height = (4 - platformlisty[i]) * unit
                        toptest =  platformlisty[i] * unit + height - 30
                    }

                    //bottomcollision
                    if (lefttest<12.5*unit - 2 && lefttest > 10.5*unit + 2 && toptest > 5*unit && toptest < 6*unit) {
                        if (timefallen == 0) {
                            fall = false

                        }
                        if (lefttest < 12.3 * unit - 3 && lefttest > 10.7 * unit + 3 && toptest > 5 * unit && top < 6 * unit){
                            height = (6 - platformlisty[i]) * unit -1
                            timefallen = 0
                            fall = false
                            canjump = true

                        }
                        toptest = platformlisty[i] * unit + height +20
                    }
                    //leftcollision
                    if (lefttest<11.5*unit && lefttest>10.5*unit && toptest > 4.3*unit && toptest < 5.8*unit) {
                        moveleft = false
                        slightleft = false
                        location = (11 - platformlistx[i]) * unit - unit/2 + 1


                    }
                    //rightcollision
                    if (lefttest<12.5*unit && lefttest>11.5*unit && toptest > 4.3*unit && toptest < 5.8*unit) {
                        moveright = false
                        slightright = false
                        location = (12 - platformlistx[i]) * unit + unit/2 - 1

                    }


                    shapeDrawable.setBounds( left, top, right, bottom)
                    shapeDrawable.getPaint().setColor(Color.parseColor(colour))
                    rectlist.add(shapeDrawable)


                }
                for (j in rectlist){
                    j.draw(canvas)
                }

                if (moveleft) {
                    location+= maxspeed
                    dinoview.rotationY = 180F
                    mydinoorientright = false

                }
                else if(slightleft) {
                    location -= magnitude
                    dinoview.rotationY = 180F
                    mydinoorientright = false
                }
                if (moveright) {
                    location-= maxspeed
                    dinoview.rotationY = 0F
                    mydinoorientright = true
                }
                else if(slightright) {
                    location -= magnitude
                    dinoview.rotationY = 0F
                    mydinoorientright = true
                }



                if (fall) {
                    timefallen++
                    if (timefallen < 25) {
                        height -= (timefallen+2) * (timefallen+2) / 30 + 3
                    }
                    else{
                        height -= 24
                    }
                }
                fall = true

            }
            override fun onFinish() {
            }
        }

        uptimer.start()





        if (bton){
            var prevsentloc = 0
            var prevsentheight = 0
            dinofriendview.visibility = View.VISIBLE
            Levelscope.launch {
                var shouldLoop = true
                while(shouldLoop) {
                    try {
                        if (location != prevsentloc || height != prevsentheight) {
                            val buffer = ByteBuffer.allocate(8)
                            buffer.putInt(location * 100 / unit)
                            buffer.putInt(height * 100 / unit)
                            val byteArray = buffer.array()
                            writeData(Bluetoothsocketholder.socket!!, byteArray)
                            delay(30)
                        }

                    } catch (e: IOException) {
                        Log.e("TAG", "Error writing to Bluetooth socket", e)
                        shouldLoop = false
                    }
                }
            }

            Levelscope.launch {
                var shouldLoop2 = true
                while (shouldLoop2) {
                    try {
                        val coordbarray = withContext(Dispatchers.IO) { readData(Bluetoothsocketholder.socket!!) }
                        val buffer = ByteBuffer.wrap(coordbarray)
                        val readloc = buffer.getInt() * unit / 100
                        friendy =  buffer.getInt() * unit / 100

                        if (readloc < friendx){
                            friendorientright = true
                        }
                        else if (readloc > friendx) {
                            friendorientright = false
                        }
                        friendx = readloc




                    } catch (e: IOException) {
                        Log.e("TAG", "Error reading Bluetooth socket", e)
                        shouldLoop2 = false
                    }


                }

            }

        }



        jumpbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hidenav(window)
                if (canjump && timefallen <= 2){
                    jump = true
                    jumptime = 0

                    /*if (timefallen > 2){
                        canjump = false
                    }*/
                    timefallen = 0
                }
                else if (canjump && timefallen > 2){
                    jump = true
                    jumptime = 0
                    timefallen = 0
                    canjump = false
                }

            }
            true
        }

        restartbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hidenav(window)
                location = 0
                height = 0
            }
            true
        }



        backbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Levelscope.cancel()
                finish()
            }
            true
        }



        stopaccidentbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hidenav(window)
            }
            true
        }


        fl.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN)  {
                hidenav(window)
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
                move = true


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





