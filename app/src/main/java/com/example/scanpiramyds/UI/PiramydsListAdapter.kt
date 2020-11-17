package com.example.scanpiramyds.UI

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiramyds.R
import com.example.scanpiramyds.database.Pyramid


class PiramydsListAdapter internal constructor(context: Context): RecyclerView.Adapter<PiramydsListAdapter.PiramydViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var piramyds = emptyList<Pyramid>()


    inner class PiramydViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        CompoundButton.OnCheckedChangeListener {

        private lateinit var mPyramid: Pyramid

        private val piramydBarcode: TextView = itemView.findViewById(R.id.piramyd_barcode)
        private val piramydName: TextView = itemView.findViewById(R.id.piramyd_name)
        private val piramydChecked: CheckBox = itemView.findViewById(R.id.piramyd_checkbox)


        fun bind(pyramid: Pyramid){
            mPyramid = pyramid

            piramydName.text = pyramid.name
            piramydBarcode.text = pyramid.code
            piramydChecked.isChecked = pyramid.checked
            piramydChecked.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            mPyramid.checked = p1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PiramydViewHolder {
        val view = layoutInflater.inflate(R.layout.piramyd_list_item, parent, false)
        return PiramydViewHolder(view)
    }

    override fun getItemCount(): Int {
       return piramyds.size
    }

    override fun onBindViewHolder(holder: PiramydViewHolder, position: Int) {
        if (position < 0 || position >= piramyds.size){
            return
        }
        holder.bind(piramyds.get(position))
    }

    internal fun  setPiramyds(pyramids:List<Pyramid>){
        this.piramyds = pyramids
        notifyDataSetChanged()
    }


}