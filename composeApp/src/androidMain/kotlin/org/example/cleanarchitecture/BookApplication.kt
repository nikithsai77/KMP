package org.example.cleanarchitecture

import android.app.Application
import org.example.cleanarchitecture.di.initKoin
import org.koin.android.ext.koin.androidContext

class BookApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BookApplication)
        }
    }

}