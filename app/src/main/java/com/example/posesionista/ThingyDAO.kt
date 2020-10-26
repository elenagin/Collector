package com.example.posesionista

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Thingy models (queries) for Room database
 */
@Dao
interface ThingyDAO {
    @Query("SELECT * from thingy_table")
    fun getAllThingys(): LiveData<List<ThingyEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(thingy: Thingy)

    @Query("DELETE FROM thingy_table")
    fun deleteAll()
}
