package com.example.posesionista

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database (from Thingy DAO and Thingy Entity)
 */
@Database(entities = [Thingy::class], version = 1, exportSchema = false)
abstract class ThingyDatabase : RoomDatabase() {

    abstract fun thingyDAO(): ThingyDAO

    companion object {
        @Volatile
        private var INSTANCE: ThingyDatabase? = null

        fun getDatabase(context: Context): ThingyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ThingyDatabase::class.java,
                    "thingy_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}