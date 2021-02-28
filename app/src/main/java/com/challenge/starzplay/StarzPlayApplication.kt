package com.challenge.starzplay

import android.app.Application
import com.challenge.datamanager.DataManager

class StarzPlayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataManager.getInstance().setKey(applicationContext,"3d0cda4466f269e793e9283f6ce0b75e")
    }
}