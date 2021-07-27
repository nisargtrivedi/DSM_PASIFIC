package com.dsm.ui.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsm.R
import com.dsm.ui.listener.*
import com.dsm.ui.model.*


class SearchItemAdapter(context: Context, jewelleryList: List<SearchItemModel>, type:Int) :
    RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {

    var jewelleryList: List<SearchItemModel> = jewelleryList
    var context:Context = context
    var type = type

    lateinit var onJwelleryClick: onSearchItemClick

    public fun onJwelleryClick(open: onSearchItemClick){
        this.onJwelleryClick = open
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var textViewName:TextView

        init {
            textViewName = view.findViewById(R.id.textViewName)
        }
    }

    override fun onBindViewHolder(
        holder: SearchItemAdapter.ViewHolder,
        position: Int
    ) {
        val task: SearchItemModel = jewelleryList.get(position)


        if(task.isSelected){
            holder.textViewName.setBackgroundResource(R.drawable.blue_border_with_white)
            holder.textViewName.setTextColor(ContextCompat.getColor(context, R.color.black));
        }else{
            holder.textViewName.setBackgroundResource(R.drawable.blue_border)
            holder.textViewName.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        holder.textViewName.setOnClickListener {
            onJwelleryClick.onClick(task)
        }
        holder.textViewName.setText(task.name)
    }

    override fun getItemCount(): Int {
        return jewelleryList.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =

            if(type==1) {
                LayoutInflater.from(context)
                    .inflate(R.layout.search_row, parent, false)
            }else{
                LayoutInflater.from(context)
                    .inflate(R.layout.search_row_two, parent, false)
            }
        return ViewHolder(itemView)

    }


}