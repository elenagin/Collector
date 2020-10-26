package com.example.posesionista

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter


private const val ARGUMENTO_COSA = "cosa_recibida"
private const val TAG = "ThingyFragment"


class ThingyFragment : Fragment() {
    private lateinit var nombreCampo: EditText
    private lateinit var precioCampo: EditText
    private lateinit var serieCampo: EditText
    private lateinit var fechaCampo: TextView
    private lateinit var botonCamara: ImageButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var editDateButton: Button
    private lateinit var vistaDeFoto: ImageView
    private lateinit var archivoFoto: File
    private lateinit var thingy: Thingy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thingy = arguments?.getParcelable(ARGUMENTO_COSA)!!
    }

    @SuppressLint("SimpleDateFormat")
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
        editDateButton = vista.findViewById(R.id.editButton)

        nombreCampo.setText(thingy.nombre)
        precioCampo.setText(thingy.valorEnPesos.toString())
        serieCampo.setText(thingy.numeroDeSerie)
        fechaCampo.isEnabled = false
        fechaCampo.text = thingy.date.toString()

        /*val pattern = "dd/MM/yyyy"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(pattern)
            fechaCampo.text = thingy.date.toString()
        } else {
            val date = Date()
            val formatter = SimpleDateFormat(pattern)
            fechaCampo.text = formatter.format(date)
        }*/

        vistaDeFoto = vista.findViewById(R.id.cameraView)
        botonCamara = vista.findViewById(R.id.cameraButton)
        buttonDelete = vista.findViewById(R.id.deleteButton)

        archivoFoto = File(
            context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "${thingy.idThingy}.jpg"
        )

        if (!archivoFoto.exists()){
            vistaDeFoto.setImageResource(R.drawable.no_image_available)
        }
        else{
            vistaDeFoto.setImageBitmap(BitmapFactory.decodeFile(archivoFoto.absolutePath))
        }

        val archivos = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.list()
        Log.d(TAG, "ArchivosExistentes: $archivos")
        return vista
    }

    override fun onStart() {
        super.onStart()
        val observador = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {
            }

            /**
             * Reacts when text of view is changed
             */
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.hashCode() == nombreCampo.text.hashCode()) {
                    thingy.nombre = s.toString()
                    if (thingy.nombre == "") {
                        Log.d(TAG, "Name empty")
                        val toast = Toast.makeText(
                            context,
                            "You can't have a Thingy without a name!",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    } else {
                        Log.d(TAG, "Name: " + thingy.nombre)
                    }
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
                if (s.hashCode() == fechaCampo.text.hashCode()) {
                    Log.d(TAG, "fechaCampo: " + fechaCampo.text)
                    try {
                        val pattern = "dd/MM/yyyy"
                        val formatter = DateTimeFormatter.ofPattern(pattern)
                        val date = LocalDate.parse(fechaCampo.text, formatter)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val current = LocalDate.now()
                            if (date.isAfter(current)) {
                                val toast = Toast.makeText(
                                    context,
                                    "Date has to be before today's date",
                                    Toast.LENGTH_LONG
                                )
                                toast.show()
                            } else {
                                Log.d("app", "Date is before today's date")
                                thingy.date = java.sql.Date.valueOf(fechaCampo.text.toString())
                                //fechaCampo.text = thingy.date.toString()
                            }
                        } else {
                            Log.e("app", "Running on a different API")
                        }
                    } catch (e: Exception) {
                        val toast = Toast.makeText(
                            context,
                            "Invalid date",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }

                }

                /**
                 * Creates files for camera
                 */
                archivoFoto = File(
                    context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "${thingy.idThingy}.jpg"
                )
                /**
                 * Gets existing files from main file directory
                 */
                vistaDeFoto.setImageBitmap(BitmapFactory.decodeFile(archivoFoto.absolutePath))
                val archivos = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.list()
                Log.d(TAG, "ArchivosExistentes: $archivos")

                //return vista
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        /**
         * Sets listeners for actions from user
         */
        nombreCampo.addTextChangedListener(observador)
        precioCampo.addTextChangedListener(observador)
        serieCampo.addTextChangedListener(observador)
        fechaCampo.addTextChangedListener(observador)

        val actividad = activity as AppCompatActivity
        actividad.supportActionBar?.setTitle(R.string.detailsThingy)

        /**
         * Camera button actions
         */
        botonCamara.apply {
            setOnClickListener {
                val intentTakingPic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                archivoFoto = creaArchivoFoto("${thingy.idThingy}.jpg")
                val fileProvider = FileProvider.getUriForFile(
                    context,
                    "com.example.posesionista.fileprovider",
                    archivoFoto
                )
                intentTakingPic.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                try {
                    startActivityForResult(intentTakingPic, 1)
                } catch (error: ActivityNotFoundException) {
                    Log.d(TAG, "Device has no camera")
                }
            }
        }

        /**
         * Delete photo button actions
         */
        buttonDelete.apply {
            setOnClickListener {
                archivoFoto =  deletePhoto("${thingy.idThingy}.jpg")
                vistaDeFoto.setImageResource(R.drawable.no_image_available)
            }
        }

        /**
         * Edit date button actions
         */
        editDateButton.apply {
            setOnClickListener {
                fechaCampo.isEnabled = true
            }
        }
    }

    /**
     * Creates route and file for photo
     */
    private fun creaArchivoFoto(nombreArchivo: String): File {
        val rutaParaArchivo = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(rutaParaArchivo, nombreArchivo)
    }

    /**
     * Deletes image file from directory
     */
    private fun deletePhoto(nombreArchivo: String): File{
        val rutaParaArchivo = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        File(rutaParaArchivo, nombreArchivo).delete()
        return File(rutaParaArchivo, nombreArchivo)
    }

    /**
     * Sends photo file to the view
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
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