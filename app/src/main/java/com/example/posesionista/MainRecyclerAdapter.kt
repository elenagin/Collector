package com.example.posesionista

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter to insert data into list
 */
class MainRecyclerAdapter(private val myDataset: Array<String>) :
    RecyclerView.Adapter<MainRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    /**
     * Creates a new view
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.section_header, parent, false) as TextView
        return MyViewHolder(textView)
    }

    /**
     * Replaces the contents of the view
     */    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = myDataset[position]
    }

    override fun getItemCount() = myDataset.size
}
