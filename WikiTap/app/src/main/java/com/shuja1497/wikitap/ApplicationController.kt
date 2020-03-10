package com.shuja1497.wikitap

import android.app.Application
import timber.log.Timber


class ApplicationController : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}