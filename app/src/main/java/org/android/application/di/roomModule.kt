package org.android.application.di

/**
 * Created by Parth Patibandha on 01,November,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

import androidx.room.Room
import org.android.application.data.database.AppDatabase
import org.android.application.presentation.utility.AppConstant
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module


val roomModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            AppConstant.DB_NAME
        ).build()
    }

    single { get<AppDatabase>().appDao() }

}