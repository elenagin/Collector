package com.example.posesionista

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


private const val TAG = "MainActivity"

//THIS CODE IS THE ONE FOR PRACTICA 3
class MainActivity : AppCompatActivity(), TablaCosasFragment.Callback {

    //var doubleBackToExitOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val message = savedInstanceState.getString("message")
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
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

    /*override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.d(TAG, "SAving here yey")
        super.onSaveInstanceState(outState, outPersistentState)
    }*/

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("message", "This is my message to be reloaded")
        outState.putParcelable()
        super.onSaveInstanceState(outState)
    }

}