package com.example.posesionista

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


private const val TAG = "MainActivity"

/**
 * Main Activity
 */
class MainActivity : AppCompatActivity(), TablaCosasFragment.Callback {

    /**
     * Overrides onCreate, checking savedInstanceState first for fragments
     */
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

    /*
     * Overrides onSaveInstanceState, allowing to save in background
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("message", "This is my message to be reloaded")
        //outState.putParcelable()
        super.onSaveInstanceState(outState)
    }

}