package org.android.application.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import org.android.application.data.models.CityItem

/**
 * Created by Parth Patibandha on 01,November,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: CityItem)

    @Query("SELECT * FROM cities")
    fun getCityListLocal(): LiveData<List<CityItem>>

    @Delete
    fun deleteCity(city: CityItem)

}