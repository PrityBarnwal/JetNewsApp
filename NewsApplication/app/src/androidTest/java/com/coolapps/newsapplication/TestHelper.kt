package com.coolapps.newsapplication

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.coolapps.newsapplication.ui.utils.NewsApp

import com.coolapps.newsapplication.utils.WindowSize

fun ComposeContentTestRule.launchNewsApplication(context: Context) {
    setContent {
       NewsApp(
           appContainer =TestAppContainer(context),
           windowSize = WindowSize.Compact)
    }
}