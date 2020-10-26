package com.example.posesionista

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.section_header.*

private const val TAG = "TablaCosasFragment"

class Section : Fragment() {

    interface Callback {
        fun onCosaSeleccionada(section: Section)
    }

    private var callbacks: Callback? = null
    private lateinit var recyclerRow: RecyclerView
    private var adapter: SectionAdapter? = null

    /**
     * Sets view model
     */
    private val sectionViewModel: SectionViewModel by lazy {
        ViewModelProvider(this).get(SectionViewModel::class.java)
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
    }

    /**
     * Creates new fragment view for object in list
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.section_header, container, false)
        recyclerRow = view.findViewById(R.id.recycler_row) as RecyclerView
        recyclerRow.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Updates for Holder
     */
    private inner class CosaHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val nombreTextView: TextView = itemView.findViewById(R.id.label_name)

        private lateinit var section: Section

        /**
         * onClick callbacks passing Section as a parameter
         */
        init {
            view.setOnClickListener(this)
        }

        /**
         * Joins view holder to data in view model, it can modify the view based on variables from model
         */
        fun bind(section: Section) {
            this.section = section
            nombreTextView.text = section.section_header.toString()

        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }
    }

    /**
     * Adapter for RecyclerView. Adapter managers the view holder and joins the view holders to their data
     */
    private inner class SectionAdapter(var inventario: List<Section>) :
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
            val section = inventario[position]
            holder.bind(section)
        }

    }
}
