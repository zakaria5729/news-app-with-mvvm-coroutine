package com.zakariahossain.newsmvvm

import android.app.Application
import com.zakariahossain.newsmvvm.data.api.RetrofitInstance

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.init(this)
    }
}