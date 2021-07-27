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


class JewelleryListModelAdapter(context: Context, jewelleryList: List<JModels>) :
    RecyclerView.Adapter<JewelleryListModelAdapter.ViewHolder>() {

    var jewelleryList: List<JModels> = jewelleryList
    var context:Context = context

    lateinit var onJwelleryClick: onJwelleryModelClick

    public fun onJwelleryClick(open: onJwelleryModelClick){
        this.onJwelleryClick = open
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvJewelleryName:TextView
        var imgJewellery: ImageView
        var tvPrice:TextView
        var tvDetails:TextView

        init {
            tvJewelleryName = view.findViewById(R.id.tvJewelleryName)
            imgJewellery = view.findViewById(R.id.imgJewellery)
            tvPrice=view.findViewById(R.id.tvPrice)
            tvDetails=view.findViewById(R.id.tvDetails)
        }
    }

    override fun onBindViewHolder(
        holder: JewelleryListModelAdapter.ViewHolder,
        position: Int
    ) {
        val task: JModels = jewelleryList.get(position)

        holder.tvJewelleryName.setText(task.modelNo)

        if(!task.image!!.imagePath.isNullOrEmpty()) {
            Glide.with(context)
                .load(task.image!!.imagePath)
                .into(holder.imgJewellery);
        }else{
            Glide.with(context)
                .load(R.drawable.noimage)
                .into(holder.imgJewellery);
        }
        holder.tvPrice.text = task.price
        //holder.imgJewellery.setText(task.PPCT)

        holder.tvDetails.setOnClickListener {
            onJwelleryClick.onClick(task)
        }

    }

    override fun getItemCount(): Int {
        return jewelleryList.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(context)
            .inflate(R.layout.jewellery_list_row, parent, false)
        return ViewHolder(itemView)

    }


}