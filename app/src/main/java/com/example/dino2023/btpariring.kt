package com.example.dino2023

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.w3c.dom.Text
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView


class Btpairing : AppCompatActivity() {
    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    @SuppressLint("ClickableViewAccessibility", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.hide()

        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.setSystemUiVisibility(uiOptions)



        setContentView(R.layout.pairing)
        val serverbutton:Button = findViewById(R.id.server)
        val refbutton:Button = findViewById(R.id.scan)
        val paired : RecyclerView = findViewById(R.id.paired)



        val backbutton: Button = findViewById(R.id.back2)
        val layout : FrameLayout = findViewById(R.id.mainlayout)
        layout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                decorView.setSystemUiVisibility(uiOptions)
            }
            true
        }
        backbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish()
            }
            true
        }
        refbutton.setOnTouchListener{ _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish();
                startActivity(getIntent());
            }
            true
        }




        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true

            if(canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }

        val tv : TextView = findViewById(R.id.tv)
        val badapter = BluetoothAdapter.getDefaultAdapter()
        if (badapter.isEnabled) {
            val devices = badapter.bondedDevices

            for (i in devices){
                val name = i.name
                val address = i.address
                tv.append("\n $name")
            }
        }









    }
}