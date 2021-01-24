package org.android.application.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.android.application.data.models.CityItem

/**
 * Created by Parth Patibandha on 01,November,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

@Database(entities = [CityItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}