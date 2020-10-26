package com.example.posesionista.database

import androidx.lifecycle.LiveData
import com.example.posesionista.Thingy

/**
 * Repository for Room database. Interacts with Room database on behalf of the ViewModel
 * (so it has DAO methods)
 */
class ThingyRepository(private val thingyDao: ThingyDAO) {

    val inventario: LiveData<List<ThingyEntity>> = thingyDao.getAllThingys()

    fun insert(thingy: Thingy) {
        thingyDao.insert(thingy)
    }

    fun deleteAll() {
        thingyDao.deleteAll()
    }
}
