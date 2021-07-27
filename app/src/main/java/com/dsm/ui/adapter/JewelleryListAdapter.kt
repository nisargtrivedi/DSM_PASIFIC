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
import com.dsm.ui.model.InventoryModel
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.JewelleryModel


class JewelleryListAdapter(context: Context, jewelleryList: List<JewelleryModel>) :
    RecyclerView.Adapter<JewelleryListAdapter.ViewHolder>() {

    var jewelleryList: List<JewelleryModel> = jewelleryList
    var context:Context = context

    lateinit var onJwelleryClick: onJwelleryDetailClick

    public fun onJwelleryClick(open: onJwelleryDetailClick){
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
        holder: JewelleryListAdapter.ViewHolder,
        position: Int
    ) {
        val task: JewelleryModel = jewelleryList.get(position)

        holder.tvJewelleryName.setText(task.jewelleryfrontproductname)

        if(!task.jewelleryfilename.isNullOrEmpty()) {
            Glide.with(context)
                .load(task.jewelleryfilename)
                .into(holder.imgJewellery);
        }else{
            Glide.with(context)
                .load(R.drawable.noimage)
                .into(holder.imgJewellery);
        }
        holder.tvPrice.text = "AUD $ "+task.jewelleryfrontcostaud
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