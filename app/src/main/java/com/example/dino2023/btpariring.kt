package com.example.dino2023

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
import android.app.ProgressDialog
import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dino2023.databinding.PairingBinding
import java.io.IOException
import java.util.*


@SuppressLint("MissingPermission")
public class Btpairing : AppCompatActivity() {



    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    companion object {
        var MY_UUID: UUID = UUID.fromString("9f6fc253-c1c7-4dad-ae71-1b4feaa3b414")
        var NAME: String = "Dinoconnection"
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }

    private var nameList : MutableList<SampleModel> = mutableListOf()

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
        val refbutton:Button = findViewById(R.id.refresh)
        val paired : RecyclerView = findViewById(R.id.paired)
        val plus: Button = findViewById(R.id.plus)
        val minus: Button = findViewById(R.id.minus)
        val pairbutton : Button = findViewById(R.id.pair)
        val devnum: TextView = findViewById(R.id.devicenum)
        var selected: Int = 1

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

        val binding: PairingBinding = PairingBinding.inflate(layoutInflater)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        var num : Int = 1

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
        }
        if (pairedDevices != null) {
            for (i in pairedDevices){
                val name = i.name
                val address = i.address
                nameList.add(SampleModel(num, name, address))
                num++
            }
        }



        val sampleAdapter: SampleAdapter = SampleAdapter(nameList)
        binding.apply {
            paired.apply {
                layoutManager= LinearLayoutManager(this@Btpairing)
                adapter=sampleAdapter
            }
        }



        plus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (selected < num - 1)
                selected++
                devnum.text = selected.toString()
            }
            true
        }
        minus.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (selected > 1){
                    selected--
                    devnum.text = selected.toString()
                }

            }
            true
        }
        pairbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                /*val intent = Intent(this@Btpairing, Select::class.java)
                val address = nameList[selected - 1].address
                intent.putExtra("EXTRA_ADDRESS", address)
                startActivity(intent)*/
                Toast.makeText(this@Btpairing, "BT connection is being established. If unsuccessful, the game will crash :)", Toast.LENGTH_LONG).show()
                val device = pairedDevices?.elementAt(selected - 1)
                val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
                    device?.createRfcommSocketToServiceRecord(MY_UUID) }
                bluetoothAdapter?.cancelDiscovery()

                mmSocket?.let { socket ->
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect()
                    Bluetoothsocketholder.socket = socket
                    val intent = Intent(this@Btpairing, Select::class.java)
                    intent.putExtra("btsocket", true)
                    intent.putExtra("server", true)
                    startActivity(intent)
                }





            }
            true
        }

        serverbutton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {

                Toast.makeText(this@Btpairing, "BT connection is being established. If unsuccessful, the game will crash :)", Toast.LENGTH_LONG).show()
                val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
                    bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID)
                }
                var shouldLoop = true
                while (shouldLoop) {
                    val socket: BluetoothSocket? = try {
                        mmServerSocket?.accept()
                    } catch (e: IOException) {

                        Log.e("TAG", "Socket's accept() method failed", e)
                        shouldLoop = false
                        null
                    }
                    socket?.also {
                        mmServerSocket?.close()
                        shouldLoop = false

                        Bluetoothsocketholder.socket = socket
                        val intent = Intent(this@Btpairing, Select::class.java)
                        intent.putExtra("btsocket", true)
                        intent.putExtra("server", true)
                        startActivity(intent)
                    }
                }
            }
            true
        }




    }

}