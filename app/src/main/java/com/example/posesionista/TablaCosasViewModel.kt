package com.example.posesionista

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class TablaCosasViewModel : ViewModel() {
    val inventario = mutableListOf<Thingy>()
    private val nombreAlAzar = arrayOf("Map", "Library", "Shelf")
    private val adjetivoAlAzar = arrayOf("Big", "Old", "Expensive")

    init {
        for (i in 0 until 20) {
            val cosaAlAzar = Thingy()
            val nombre = nombreAlAzar.random()
            val adjetivo = adjetivoAlAzar.random()
            val precio = Random.nextInt(1000)
            cosaAlAzar.nombre = "$nombre $adjetivo"
            cosaAlAzar.valorEnPesos = precio
            inventario += cosaAlAzar
        }
        //inventario.sortedBy { it.valorEnPesos }
        //val sorted =
    }

    fun remove(pos: Int){
        inventario.removeAt(pos)
    }
}



