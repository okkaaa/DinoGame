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


class MyBluetoothService(
    private val socket: BluetoothSocket
) {
    fun listenForIncomingMessages(): Flow<String> {
        return flow {
            if(!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(1024)
            while(true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch(e: IOException) {
                    Log.d("TAG", "Input stream was disconnected", e)

                }
                emit(
                    buffer.decodeToString(endIndex = byteCount)

                )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(bytes)
            } catch(e: IOException) {
                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }
}
