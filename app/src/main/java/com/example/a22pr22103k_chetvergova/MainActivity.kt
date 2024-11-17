package com.example.a22pr22103k_chetvergova

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.a22pr22103k_chetvergova.ui.theme._22pr22103k_chetvergovaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _22pr22103k_chetvergovaTheme {
                GridScreen(cols = 6, rows = 6, picturePrefix = "anim")
            }
        }
    }
}