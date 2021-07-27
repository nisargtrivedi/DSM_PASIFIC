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
import com.dsm.ui.listener.onDialogClick
import com.dsm.ui.listener.onJwelleryClick
import com.dsm.ui.listener.onJwelleryDetailClick
import com.dsm.ui.listener.onJwelleryModelClick
import com.dsm.ui.model.InventoryModel
import com.dsm.ui.model.JModels
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.JewelleryModel


class DetailImageAdapter(context: Context, jewelleryList: List<String>) :
    RecyclerView.Adapter<DetailImageAdapter.ViewHolder>() {

    var jewelleryList: List<String> = jewelleryList
    var context:Context = context

    lateinit var onJwelleryClick: onJwelleryModelClick



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var imgJewellery: ImageView

        init {
            imgJewellery = view.findViewById(R.id.imgDiamond)
        }
    }

    override fun onBindViewHolder(
        holder: DetailImageAdapter.ViewHolder,
        position: Int
    ) {
        val task: String = jewelleryList.get(position)



        if(!task!!.isNullOrEmpty()) {
            Glide.with(context)
                .load(task!!)
                .into(holder.imgJewellery);
        }else{
            Glide.with(context)
                .load(R.drawable.noimage)
                .into(holder.imgJewellery);
        }

    }

    override fun getItemCount(): Int {
        return jewelleryList.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(context)
            .inflate(R.layout.row_image, parent, false)
        return ViewHolder(itemView)

    }


}