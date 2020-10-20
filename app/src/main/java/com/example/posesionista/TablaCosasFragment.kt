package com.example.posesionista

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Color.green
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cosa_layout.*
import java.util.*

private const val TAG = "TablaCosasFragment"

class TablaCosasFragment : Fragment() {

    interface Callback {
        fun onCosaSeleccionada(unaCosa: Thingy)
    }

    private var callbacks: Callback? = null
    private lateinit var cosaRecyclerView: RecyclerView
    private var adapter: CosaAdapter? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d(TAG, "Total de posesiones: ${tablaCosasViewModel.inventario.size}")
    }

    companion object {
        fun newInstance(): TablaCosasFragment {
            return TablaCosasFragment()
        }
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //inflater.inflate(R.menu.menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /*R.id.menuAddBtton -> {
                val thingy= Thingy()
                tablaCosasViewModel.addThingy(thingy)
                callbacks?.onCosaSeleccionada(thingy)
                return true
            }*/
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun actualizaUI() {
        val inventario = tablaCosasViewModel.inventario
        adapter = CosaAdapter(inventario)
        cosaRecyclerView.adapter = adapter
    }

    private inner class CosaHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val nombreTextView: TextView = itemView.findViewById(R.id.label_name)
        val precioTextView: TextView = itemView.findViewById(R.id.label_price)
        val serieTextView: TextView = itemView.findViewById(R.id.label_series)
        val cardView: CardView = itemView.findViewById(R.id.card)

        private lateinit var thingy: Thingy

        init {
            view.setOnClickListener(this)
        }

        fun bind(thingy: Thingy) {
            this.thingy = thingy
            nombreTextView.text = thingy.nombre
            precioTextView.text = "$ " + thingy.valorEnPesos.toString()
            serieTextView.text = thingy.numeroDeSerie
            if (thingy.valorEnPesos in 0..99) {
                cardView.setCardBackgroundColor(Color.parseColor("#EDC7BF"))
            } else if (thingy.valorEnPesos in 100..199) {
                cardView.setCardBackgroundColor(Color.parseColor("#E9B9AF"))
            } else if (thingy.valorEnPesos in 200..299) {
                cardView.setCardBackgroundColor(Color.parseColor("#E5AB9F"))
            } else if (thingy.valorEnPesos in 300..399) {
                cardView.setCardBackgroundColor(Color.parseColor("#E09C8F"))
            } else if (thingy.valorEnPesos in 400..499) {
                cardView.setCardBackgroundColor(Color.parseColor("#DC8E7F"))
            } else if (thingy.valorEnPesos in 500..599) {
                cardView.setCardBackgroundColor(Color.parseColor("#D8806F"))
            } else if (thingy.valorEnPesos in 600..699) {
                cardView.setCardBackgroundColor(Color.parseColor("#D3725F"))
            } else if (thingy.valorEnPesos in 700..799) {
                cardView.setCardBackgroundColor(Color.parseColor("#CF644F"))
            } else if (thingy.valorEnPesos in 800..899) {
                cardView.setCardBackgroundColor(Color.parseColor("#CA563F"))
            } else if (thingy.valorEnPesos in 900..1000) {
                cardView.setCardBackgroundColor(Color.parseColor("#C04C35"))
            }


        }

        override fun onClick(view: View?) {
            callbacks?.onCosaSeleccionada(thingy) //algo opcional
            Log.d("IN ON CLICK", "IN ON CLICK")
        }
    }

    private inner class CosaAdapter(var inventario: List<Thingy>) :
        RecyclerView.Adapter<CosaHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CosaHolder {
            val vista = layoutInflater.inflate(R.layout.cosa_layout, parent, false)
            return CosaHolder(vista)
        }

        override fun getItemCount() = inventario.size

        override fun onBindViewHolder(holder: CosaHolder, position: Int) {
            val thingy = inventario[position]
            holder.bind(thingy) //le paso thingy
        }

    }

    private fun configuraItemTouchHelper() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition
                Collections.swap(tablaCosasViewModel.inventario, sourcePosition, targetPosition)
                adapter?.notifyItemMoved(sourcePosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val dialogBuilder = AlertDialog.Builder(context)
                    dialogBuilder.setMessage("Are you sure you want to delete this Thingy?")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Proceed"
                        ) { dialog, id ->
                            dialog.run {
                                tablaCosasViewModel.remove(viewHolder.adapterPosition)
                                cosaRecyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                                Log.d(TAG, "Deleted")
                            }
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            dialog.cancel()
                        }

                    val alert = dialogBuilder.create()
                    alert.setTitle("Delete Thingy?")
                    alert.show()
                }
            }

        }

        val gestureDetector = ItemTouchHelper(itemTouchCallback)
        gestureDetector.attachToRecyclerView(cosaRecyclerView)
    }
}
