package org.codejudge.application

import android.app.Application
import org.codejudge.application.di.networkModule
import org.codejudge.application.di.repositoryModule
import org.codejudge.application.di.viewModelModule
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        startKoin(
            this, arrayListOf(
                networkModule,
                repositoryModule,
                viewModelModule
            )
        )
    }
}