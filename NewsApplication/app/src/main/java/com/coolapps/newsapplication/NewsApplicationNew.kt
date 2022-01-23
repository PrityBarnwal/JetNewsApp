package com.coolapps.newsapplication

import android.app.Application
import com.coolapps.newsapplication.data.AppContainer
import com.coolapps.newsapplication.data.AppContainerImpl

class NewsApplicationNew : Application(){

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}