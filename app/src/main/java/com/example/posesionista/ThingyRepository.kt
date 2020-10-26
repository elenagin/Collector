package com.example.posesionista

import androidx.lifecycle.LiveData

/**
 * Repository for Room database. Interacts with Room database on behalf of the ViewModel
 * (so it has DAO methods)
 */
class ThingyRepository(private val thingyDao: ThingyDAO) {

    val inventario: LiveData<List<ThingyEntity>> = thingyDao.getAllThingys()

    fun insert(thingy: Thingy) {
        thingyDao.insert(thingy)
    }

    fun deleteAll(thingy: Thingy) {
        thingyDao.deleteAll()
    }
}
