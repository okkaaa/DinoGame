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




class Select : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()
        var bton = false
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

        val tv : TextView = findViewById(R.id.textView2)
        val extras = intent.extras
        if (Bluetoothsocketholder.socket != null) {
            bton = true

            val socket = Bluetoothsocketholder.socket

        }


        backbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish()
            }
            true
        }
        button1.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {

                if (bton) {
                    GlobalScope.launch {
                        try {
                            val data = "hello".toByteArray()
                            writeData(socket!!, data)
                        } catch (e: IOException) {
                            Log.e("TAG", "Error writing to Bluetooth socket", e)
                        }
                    }
                }
                val intent = Intent(this@Select, Level::class.java)
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
            /*GlobalScope.launch {
                var shouldloop: Boolean = true
                /* (shouldloop) {
                    try {
                        val data = readData(socket!!)
                        val stringdata = data.toString()
                        shouldloop = false


                    } catch (e: IOException) {
                        Log.e("TAG", "Error reading Bluetooth socket", e)
                    }
                }*/
                val buffer = ByteArray(1024)
                while(shouldloop) {
                    val byteCount = try {
                        socket!!.inputStream.read(buffer)
                        val t = buffer.decodeToString()
                        tv.text = t
                    } catch (e: IOException) {
                        Log.d("TAG", "Input stream was disconnected", e)

                    }
                }
            }*/
            GlobalScope.launch {
                var shouldLoop = true
                while (shouldLoop) {
                    try {
                        val data = withContext(Dispatchers.IO){readData(socket!!)}
                        BTMSG.data = data


                    } catch (e: IOException) {
                        Log.e("TAG", "Error reading Bluetooth socket", e)
                        shouldLoop = false
                    }
                }
            }
            GlobalScope.launch {
                var shouldloop2 = true
                while (shouldloop2){
                    if (BTMSG.data != null){
                        val datastring : String = BTMSG.data!!
                        shouldloop2 = false
                    }
                }
                runOnUiThread { tv.text = BTMSG.data!! }

            }



        }
    }
}
suspend fun readData(socket: BluetoothSocket): String {
    return withContext(Dispatchers.IO) {
        val inputStream: InputStream = socket.inputStream
        val buffer = ByteArray(1024)
        val bytesRead: Int = inputStream.read(buffer)
        buffer.copyOf(bytesRead).decodeToString()
    }
}

suspend fun writeData(socket: BluetoothSocket, data: ByteArray) {
    return withContext(Dispatchers.IO) {
        val outputStream: OutputStream = socket.outputStream
        outputStream.write(1)
        outputStream.flush()
    }
}
fun listenForIncomingMessages(socket: BluetoothSocket): Flow<String> {
    return flow {
        if(!socket!!.isConnected) {
            return@flow
        }
        val buffer = ByteArray(1024)
        while(true) {
            val byteCount = try {
                socket!!.inputStream.read(buffer)
            } catch(e: IOException) {
                Log.d("TAG", "Input stream was disconnected", e)

            }
            emit(
                buffer.decodeToString(endIndex = byteCount)

            )
        }
    }.flowOn(Dispatchers.IO)
}


