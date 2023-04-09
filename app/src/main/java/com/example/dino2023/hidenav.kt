package com.example.dino2023

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService



fun hidenav(window : Window){
    if (Build.VERSION_CODES.R <= android.os.Build.VERSION.SDK_INT) {
        val decorView = window.decorView
        window.setFlags(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.setSystemUiVisibility(uiOptions)
    }
    else {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setFlags(View.SYSTEM_UI_FLAG_FULLSCREEN, View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}