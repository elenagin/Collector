package com.example.posesionista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private const val TAG = "MainActivity"


//THIS CODE IS THE ONE FOR PRACTICA 2
class MainActivity : AppCompatActivity(), TablaCosasFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentoActual = supportFragmentManager.findFragmentById(R.id.fragmentView)
        if (fragmentoActual == null) {
            val fragment = TablaCosasFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentView, fragment)
                .commit()
        }
    }

    override fun onCosaSeleccionada(unaCosa: Thingy) {
        Log.d(TAG, "onCosaSeleccionada: ${unaCosa.nombre}")
        supportFragmentManager.beginTransaction().apply {
            val fragmento = ThingyFragment.nuevaInstancia(unaCosa)
            replace(R.id.fragmentView, fragmento)
            addToBackStack(null)
            commit()
        }
    }
}