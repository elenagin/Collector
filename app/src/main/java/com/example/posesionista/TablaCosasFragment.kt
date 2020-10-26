package com.example.posesionista

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*

private const val TAG = "TablaCosasFragment"

class TablaCosasFragment : Fragment() {

    interface Callback {
        fun onCosaSeleccionada(unaCosa: Thingy)
    }

    private var callbacks: Callback? = null
    private lateinit var cosaRecyclerView: RecyclerView
    private var adapter: CosaAdapter? = null

    /**
     * Sets view model
     */
    private val tablaCosasViewModel: TablaCosasViewModel by lazy {
        ViewModelProvider(this).get(TablaCosasViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callback
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    /**
     * Creates Fragment based on savedInstance for view model
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(TAG, "Total de posesiones: ${tablaCosasViewModel.inventario.size}")
        //tablaCosasViewModel.
    }

    /**
     * Creates new fragment instance for object in list
     */
    companion object {
        fun newInstance(): TablaCosasFragment {
            return TablaCosasFragment()
        }
    }

    /**
     * Creates new fragment view for object in list
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.lista_cosas_fragment, container, false)
        cosaRecyclerView = vista.findViewById(R.id.cosa_recycler_view) as RecyclerView
        cosaRecyclerView.layoutManager = LinearLayoutManager(context)
        configuraItemTouchHelper()
        actualizaUI()
        return vista
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Updates List for User Interface
     */
    private fun actualizaUI() {
        val inventario = tablaCosasViewModel.inventario
        adapter = CosaAdapter(inventario)
        cosaRecyclerView.adapter = adapter
    }

    /**
     * Updates for Thingy Holder
     */
    private inner class CosaHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val nombreTextView: TextView = itemView.findViewById(R.id.label_name)
        val precioTextView: TextView = itemView.findViewById(R.id.label_price)
        val serieTextView: TextView = itemView.findViewById(R.id.label_series)
        val cardView: CardView = itemView.findViewById(R.id.card)
        val imageView: ImageView = itemView.findViewById(R.id.thumbnail)
        private lateinit var archivoFoto: File


        private lateinit var thingy: Thingy

        /**
         * onClick callbacks passing Thingy as a parameter
         */
        init {
            view.setOnClickListener(this)
        }

        /**
         * Joins view holder to data in view model
         */
        fun bind(thingy: Thingy) {
            this.thingy = thingy
            nombreTextView.text = thingy.nombre
            precioTextView.text = getString(R.string.money).plus(thingy.valorEnPesos)
            serieTextView.text = thingy.numeroDeSerie
            archivoFoto = File(
                context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "${thingy.idThingy}.jpg"
            )

            if (!archivoFoto.exists()) {
                imageView.setImageResource(R.drawable.no_image_icon)
            } else {
                imageView.setImageBitmap(BitmapFactory.decodeFile(archivoFoto.absolutePath))
            }

            when (thingy.valorEnPesos) {
                in 0..99 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#EDC7BF"))
                }
                in 100..199 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#E9B9AF"))
                }
                in 200..299 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#E5AB9F"))
                }
                in 300..399 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#E09C8F"))
                }
                in 400..499 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#DC8E7F"))
                }
                in 500..599 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#D8806F"))
                }
                in 600..699 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#D3725F"))
                }
                in 700..799 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#CF644F"))
                }
                in 800..899 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#CA563F"))
                }
                in 900..1000 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#C04C35"))
                }
                in 1000..10000 -> {
                    cardView.setCardBackgroundColor(Color.parseColor("#A83923"))
                }
            }
        }

        /**
         * onClick callbacks passing Thingy as a parameter
         */
        override fun onClick(view: View?) {
            callbacks?.onCosaSeleccionada(thingy) //algo opcional
            Log.d("IN ON CLICK", "IN ON CLICK")
        }
    }

    /**
     * Adapter for RecyclerView. Adapter managers the view holder and joins the view holders to their data
     */
    private inner class CosaAdapter(var inventario: List<Thingy>) :
        RecyclerView.Adapter<CosaHolder>() {

        /**
         * Creates view for view holders
         * our view model.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CosaHolder {
            val vista = layoutInflater.inflate(R.layout.cosa_layout, parent, false)
            return CosaHolder(vista)
        }

        /**
         * Default implementation adapter getItemCount will return the total number of items in
         * our view model.
         */
        override fun getItemCount() = inventario.size

        /**
         * Joins views for view holders to our view model.
         */
        override fun onBindViewHolder(holder: CosaHolder, position: Int) {
            val thingy = inventario[position]
            holder.bind(thingy) //le paso thingy
        }

    }

    /**
     * Receives all touch & gesture actions from user and responds.
     */
    private fun configuraItemTouchHelper() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT
        ) {

            /**
             * When users rearranges (moves), will override onMove and rearrange items from
             * view as well as notify the view model.
             */
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition
                Collections.swap(tablaCosasViewModel.inventario, sourcePosition, targetPosition)
                adapter?.notifyItemMoved(sourcePosition, targetPosition)
                return true
            }

            /**
             * When users swipes, will override onSwiped and remove item from view as well as
             * update the view model.
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val dialogBuilder = AlertDialog.Builder(context)
                    dialogBuilder.setMessage("Are you sure you want to delete this Thingy?")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Proceed"
                        ) { dialog, _ ->
                            dialog.run {
                                tablaCosasViewModel.remove(viewHolder.adapterPosition)
                                val rutaParaArchivo = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                File(rutaParaArchivo, "${this}.jpg").delete()
                                Log.d(TAG, rutaParaArchivo.toString())
                                cosaRecyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                                Log.d(TAG, "Deleted")
                            }
                        }
                        .setNegativeButton(
                            "Cancel"
                        ) { dialog, _ ->
                            dialog.run {
                                cosaRecyclerView.adapter?.notifyItemChanged(viewHolder.adapterPosition)
                                Log.d(TAG, "Cancelled")
                            }
                        }

                    val alert = dialogBuilder.create()
                    alert.setTitle("Delete Thingy?")
                    alert.show()
                }
            }
        }

        /**
         * Detected gesture acts on Recycler View
         */
        val gestureDetector = ItemTouchHelper(itemTouchCallback)
        gestureDetector.attachToRecyclerView(cosaRecyclerView)
    }
}
