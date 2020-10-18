package com.example.posesionista

import androidx.lifecycle.ViewModel

class TablaCosasViewModel : ViewModel() {
    val inventario = mutableListOf<Thingy>()
    /*private val nombreAlAzar = arrayOf("Map", "Library", "Shelf")
    private val adjetivoAlAzar = arrayOf("Big", "Old", "Expensive")

    init {
        for (i in 0 until 10) {
            val cosaAlAzar = Thingy()
            val nombre = nombreAlAzar.random()
            val adjetivo = adjetivoAlAzar.random()
            val precio = Random.nextInt(100)
            cosaAlAzar.nombre = "$nombre $adjetivo"
            cosaAlAzar.valorEnPesos = precio
            inventario += cosaAlAzar
        }
    }*/

    fun remove(pos: Int){
        inventario.removeAt(pos)
    }

    fun addThingy(thingy: Thingy){
        inventario.add(thingy)
    }

}



