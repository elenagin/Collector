package com.example.posesionista

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


private const val ARGUMENTO_COSA = "cosa_recibida"
private const val TAG = "ThingyFragment"


class ThingyFragment : Fragment() {
    private lateinit var nombreCampo: EditText
    private lateinit var precioCampo: EditText
    private lateinit var serieCampo: EditText
    private lateinit var fechaCampo: TextView
    private lateinit var botonCamara: ImageButton
    private lateinit var vistaDeFoto: ImageView
    private lateinit var archivoFoto: File
    private lateinit var thingy: Thingy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thingy = arguments?.getParcelable(ARGUMENTO_COSA)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.cosa_fragment, container, false)
        nombreCampo = vista.findViewById(R.id.campoNombre)
        precioCampo = vista.findViewById(R.id.campoPrecio)
        serieCampo = vista.findViewById(R.id.campoSerie)
        fechaCampo = vista.findViewById(R.id.campoFecha)

        nombreCampo.setText(thingy.nombre)
        precioCampo.setText(thingy.valorEnPesos.toString())
        serieCampo.setText(thingy.numeroDeSerie)
        //fechaCampo.text = thingy.date.toString()


        val pattern = "dd-MM-yyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(thingy.date)
        fechaCampo.setText(date)
        vistaDeFoto = vista.findViewById(R.id.cameraView)
        botonCamara = vista.findViewById(R.id.cameraButton)
        archivoFoto = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${thingy.idThingy}.jpg")
        vistaDeFoto.setImageBitmap(BitmapFactory.decodeFile(archivoFoto.absolutePath))
        val archivos = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.list()
        Log.d(TAG, "ArchivosExistentes: $archivos")
        return vista
    }

    override fun onStart() {
        super.onStart()
        val observador = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.hashCode() == nombreCampo.text.hashCode()) {
                    thingy.nombre = s.toString()
                }
                if (s.hashCode() == precioCampo.text.hashCode()) {
                    if (s != null) {
                        if (s.isEmpty()) {
                            thingy.valorEnPesos = 0
                        } else {
                            thingy.valorEnPesos = s.toString().toInt()
                        }
                    }
                }
                if (s.hashCode() == serieCampo.text.hashCode()) {
                    Log.d("CosaFragment", "Cambiando numero de serie ${s.toString()}")
                    thingy.numeroDeSerie = s.toString()
                }
                if (s.hashCode() == serieCampo.text.hashCode()) {
                    thingy.numeroDeSerie = s.toString()
                }
                archivoFoto = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${thingy.idThingy}.jpg")
                vistaDeFoto.setImageBitmap(BitmapFactory.decodeFile(archivoFoto.absolutePath))
                val archivos = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.list()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        nombreCampo.addTextChangedListener(observador)
        precioCampo.addTextChangedListener(observador)
        serieCampo.addTextChangedListener(observador)

        val actividad = activity as AppCompatActivity
        actividad.supportActionBar?.setTitle(R.string.detailsThingy)

        botonCamara.apply {
            setOnClickListener {
                val intentTakingPic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                archivoFoto = creaArchivoFoto("${thingy.idThingy}.jpg")
                val fileProvider = FileProvider.getUriForFile(context, "com.example.posesionista.fileprovider", archivoFoto)
                intentTakingPic.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                try {
                    startActivityForResult(intentTakingPic, 1)
                } catch (error: ActivityNotFoundException){
                   Log.d(TAG, "Device has no camera")
                }
            }
        }
    }

    private fun creaArchivoFoto(nombreArchivo: String): File{
        val rutaParaArchivo = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(rutaParaArchivo, nombreArchivo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == 1 && resultCode == RESULT_OK){
            val fotoBitmap = BitmapFactory.decodeFile(archivoFoto.absolutePath)
            vistaDeFoto.setImageBitmap(fotoBitmap)
        }
    }


    companion object {
        fun nuevaInstancia(thingy: Thingy): ThingyFragment {
            val argumentos = Bundle().apply {
                putParcelable(ARGUMENTO_COSA, thingy)
            }
            return ThingyFragment().apply {
                arguments = argumentos
            }
        }
    }

}