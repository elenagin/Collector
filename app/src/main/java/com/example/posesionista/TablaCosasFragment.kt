package com.example.posesionista

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        inflater.inflate(R.menu.menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menuAddBtton -> {
                val thingy= Thingy()
                tablaCosasViewModel.addThingy(thingy)
                callbacks?.onCosaSeleccionada(thingy)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun actualizaUI() {
        val inventario = tablaCosasViewModel.inventario
        adapter = CosaAdapter(inventario)
        cosaRecyclerView.adapter = adapter
    }

    private inner class CosaHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{
        val nombreTextView: TextView = itemView.findViewById(R.id.label_nombre)
        val precioTextView: TextView = itemView.findViewById(R.id.label_precio)
        private lateinit var thingy: Thingy

        init {
            view.setOnClickListener(this)
        }

        fun bind (thingy: Thingy){
            this.thingy = thingy
            nombreTextView.text = thingy.nombre
            precioTextView.text = thingy.valorEnPesos.toString()
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
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    tablaCosasViewModel.remove(viewHolder.adapterPosition)
                    cosaRecyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                }
            }
        }

        val gestureDetector = ItemTouchHelper(itemTouchCallback)
        gestureDetector.attachToRecyclerView(cosaRecyclerView)
    }
}