package com.example.dino2023

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


suspend fun readData(socket: BluetoothSocket): String {
    return withContext(Dispatchers.IO) {
        val inputStream: InputStream = socket.inputStream
        val buffer = ByteArray(16)
        val bytesRead: Int = inputStream.read(buffer)
        buffer.copyOf(bytesRead).decodeToString()
    }
}

suspend fun writeData(socket: BluetoothSocket, data: ByteArray) {
    return withContext(Dispatchers.IO) {
        val outputStream: OutputStream = socket.outputStream
        outputStream.write(data)
        outputStream.flush()
    }
}


