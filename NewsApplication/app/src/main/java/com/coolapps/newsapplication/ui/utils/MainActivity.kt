package com.coolapps.newsapplication.ui.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.coolapps.newsapplication.NewsApplicationNew
import com.coolapps.newsapplication.utils.rememberWindowSizeClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val appContainer = (application as NewsApplicationNew).container
        setContent {
            val windowSizeClass = rememberWindowSizeClass()
            NewsApp(appContainer, windowSizeClass)
        }
    }
}
