package com.example.dino2023

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.example.dino2023.BTMSG.Companion.data
import com.example.dino2023.Bluetoothsocketholder.Companion.socket
import kotlinx.coroutines.*
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.logging.Logger.global



private var bton = false //if Bluetooth is on
private var BTLOOP = true //loop bluetooth read
private val selectScope = CoroutineScope(Dispatchers.Main)


class Select() : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()


        var instream: InputStream?
        var outstream: OutputStream?

        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.setSystemUiVisibility(uiOptions)

        setContentView(R.layout.levelselect)
        val layout: FrameLayout = findViewById(R.id.mainlayout1)
        val backbutton: Button = findViewById(R.id.back)
        val button1: Button = findViewById(R.id.level1)
        val button2: Button = findViewById(R.id.level2)

        val tv : TextView = findViewById(R.id.textView2)


        if (Bluetoothsocketholder.socket != null) {
            bton = true
        }


        backbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish()
            }
            true
        }
        button1.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                BTLOOP = false
                if (bton){
                    buttonpressbt(1)
                }
                val intent = Intent(this@Select, Level::class.java)
                intent.putExtra("level", 1)
                startActivity(intent)
            }
            true
        }
        button2.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                BTLOOP = false
                if (bton){
                    buttonpressbt(2)
                }
                val intent = Intent(this@Select, Level::class.java)
                intent.putExtra("level", 2)
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

        if (bton){
            selectScope.launch {
                while (BTLOOP){
                    try {
                        val data = withContext(Dispatchers.IO) { readData(socket!!) }
                        if (data.length > 1){
                            continue
                        }
                        else {
                            BTMSG.data = data.toInt()
                            BTLOOP = false
                            runOnUiThread {
                                val intent = Intent(this@Select, Level::class.java)
                                intent.putExtra("level", BTMSG.data)
                                startActivity(intent)
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("TAG", "Error reading Bluetooth socket", e)
                        BTLOOP = false
                    }
                }

            }
        }
    }
}
fun buttonpressbt (n: Int) {
    selectScope.launch {
        try {
            val str : String = n.toString()
            val data = str.toByteArray()
            writeData(socket!!, data)
        } catch (e: IOException) {
            Log.e("TAG", "Error writing to Bluetooth socket", e)
        }

    }
}









