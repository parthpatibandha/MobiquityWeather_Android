package org.android.application

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs
import net.danlew.android.joda.JodaTimeAndroid
import org.android.application.di.networkModule
import org.android.application.di.repositoryModule
import org.android.application.di.roomModule
import org.android.application.di.viewModelModule
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
                viewModelModule,
                roomModule
            )
        )

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        JodaTimeAndroid.init(this)
    }
}