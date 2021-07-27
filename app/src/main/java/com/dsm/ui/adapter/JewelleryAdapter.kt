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
import com.dsm.ui.model.InventoryModel
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.JewelleryModel


class JewelleryAdapter(context: Context, jewelleryList: List<JewelleryCategoryModel>) :
    RecyclerView.Adapter<JewelleryAdapter.ViewHolder>() {

    var jewelleryList: List<JewelleryCategoryModel> = jewelleryList
    var context:Context = context

    lateinit var onJwelleryClick: onJwelleryClick

    public fun onJwelleryClick(open:onJwelleryClick){
        this.onJwelleryClick = open
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvJewelleryName:TextView
        var imgJewellery: ImageView

        init {
            tvJewelleryName = view.findViewById(R.id.tvJewelleryName)
            imgJewellery = view.findViewById(R.id.imgJewellery)
        }
    }

    override fun onBindViewHolder(
            holder: JewelleryAdapter.ViewHolder,
            position: Int
    ) {
        val task: JewelleryCategoryModel = jewelleryList.get(position)

        holder.tvJewelleryName.setText(task.attribute_name)

        if(!task.attribute_image_path.isNullOrEmpty()) {
            Glide.with(context)
                .load(task.attribute_image_path)
                .into(holder.imgJewellery);
        }else{
            Glide.with(context)
                .load(R.drawable.noimage)
                .into(holder.imgJewellery);
        }
        //holder.imgJewellery.setText(task.PPCT)

        holder.tvJewelleryName.setOnClickListener {
            onJwelleryClick.onClick(task)
        }









//        Picasso.with(context)
//            .load(R.drawable.nisarg)
//            .transform(RoundedCornersTransform())
//            .into(holder.imgPost);
//        Glide.with(context)
//            .load(R.drawable.nisarg)
//            .into(holder.imgPost)
//        val roundingParams = RoundingParams.fromCornersRadius(30f)
//        holder.imgPost.setHierarchy(
//            GenericDraweeHierarchyBuilder(context.resources)
//                .setRoundingParams(roundingParams)
//                .build()
//        )
//        //holder.imgPost.setActualImageResource(R.drawable.nisarg)
//        var imgURI = Uri.parse("https://miro.medium.com/fit/c/240/240/1*SF2VIRFshYt2etl6OhNm_Q.png");
//        var imgURI = Uri.parse(task.attribute_image_path);
//                //holder.imgPost.setImageURI(imgURI,context);
//        holder.imgJewellery.setImageURI(imgURI);
//
//
//
//        val color: Int = ContextCompat.getColor(context,R.color.image_border)
//        val roundingParams2 = RoundingParams.fromCornersRadius(5f)
//        roundingParams2.setBorder(color, 3.0f)
//        roundingParams2.roundAsCircle = true
//        holder.imgUser.getHierarchy().setRoundingParams(roundingParams2)

    }

    override fun getItemCount(): Int {
        return jewelleryList.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(context)
            .inflate(R.layout.jewellery_row, parent, false)
        return ViewHolder(itemView)

    }


}