package com.dsm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.dsm.R
import com.dsm.ui.listener.onDialogClick
import com.dsm.ui.model.DiamondModel
import com.dsm.ui.model.PermissionModel


class InventoryAdapter(context: Context, usersList: List<DiamondModel>) :
    RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    var usersList: List<DiamondModel>
    var context:Context
    lateinit var permissionModel: PermissionModel
    init {
        this.context =context
        this.usersList= usersList
    }
    lateinit var openDialog : onDialogClick

    public fun DialogOpen(open: onDialogClick){
        this.openDialog = open
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvShape: TextView
        var tvCarat : TextView
        var tvColor: TextView
        var tvClarity:TextView

        var tvLBH:TextView
        var tvCut:TextView
        var tvFLR:TextView
        var tvTable:TextView
        var tvDepth:TextView
        var tvGIA:TextView

        var tvLot:TextView
        var tvPPCT:TextView
        var tvTotal:TextView
        var tvINS:TextView
        var chk: ImageView
        var llImage:LinearLayout
        var llStatus : LinearLayout

        init {
            tvShape = view.findViewById(R.id.tvShape)
            tvCarat = view.findViewById(R.id.tvCarat)
            tvColor = view.findViewById(R.id.tvColor)
            tvClarity = view.findViewById(R.id.tvClarity)

            tvCut = view.findViewById(R.id.tvCut)
            tvLBH = view.findViewById(R.id.tvLBH)

            tvFLR =view.findViewById(R.id.tvFLR)
            tvTable =view.findViewById(R.id.tvTable)

            tvDepth =view.findViewById(R.id.tvDepth)
            tvGIA =view.findViewById(R.id.tvGIA)
            tvLot =view.findViewById(R.id.tvLot)
            tvPPCT =view.findViewById(R.id.tvPPCT)
            tvTotal =view.findViewById(R.id.tvTotal)
            tvINS =view.findViewById(R.id.tvINS)
            chk = view.findViewById(R.id.chk)
            llImage=view.findViewById(R.id.llImage)
            llStatus=view.findViewById(R.id.llStatus)
        }
    }


    override fun onBindViewHolder(
        holder: InventoryAdapter.ViewHolder,
        position: Int
    ) {
        val task: DiamondModel = usersList.get(position)


        holder.llImage.setOnClickListener {
            openDialog.onDialogOpen(task)
        }
//        holder.chk.setOnClickListener {
//            if (task.isCheched == 1)
//                task.isCheched = 0
//            else
//                task.isCheched = 1
//
//            if (task.isCheched == 1) {
//                holder.chk.post {
//                    holder.chk.setImageResource(R.drawable.ic_check)
//                }
//            } else {
//                holder.chk.post {
//                    holder.chk.setImageResource(R.drawable.blue_border)
//                }
//            }
//        }
//        if(task.isCheched==1){
//            holder.chk.post {
//                holder.chk.setImageResource(R.drawable.ic_check)
//            }
//        }else{
//            holder.chk.post {
//                holder.chk.setImageResource(R.drawable.blue_border)
//            }
//        }
        holder.tvShape.setText(task.diamond_shape)
        holder.tvCarat.setText(task.diamond_size)
        holder.tvColor.setText(task.diamond_clr)
        holder.tvClarity.setText(task.diamond_cla)
        holder.tvCut.setText(task.diamond_fcut)
        holder.tvLBH.setText(task.diamond_meas1 + "x" + task.diamond_meas2 + "x" + task.diamond_meas3)
        holder.tvFLR.setText(task.diamond_flr)
        holder.tvTable.setText(task.diamond_tab)
        holder.tvDepth.setText(task.diamond_dep)
        holder.tvGIA.setText(task.diamond_cert)

        holder.tvGIA.setOnClickListener {
            if(!task.certificate_link.isNullOrEmpty()) {
                val url = task.certificate_link
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(i)
            }
        }

        holder.tvLot.setText(
            task.diamond_lot_no + "\n" +
                    if (permissionModel.show_location) {
                        task.location
                    } else {

                    }
        )
        if(task.diamond_status=="upcoming"){
            holder.llStatus.setBackgroundColor(Color.parseColor("#5E541A"))
        }else{
            holder.llStatus.setBackgroundColor(Color.TRANSPARENT)
        }
        if(permissionModel.user_show_price){
            if(!permissionModel.show_price_aud){
                holder.tvTotal.setText(task.diamond_selling_price)
                holder.tvPPCT.setText(task.perct_price)
            }else{
                holder.tvTotal.setText(task.diamond_price_sell_AUD)
                holder.tvPPCT.text = "-"
            }
        }else{
            if(permissionModel.show_price_aud){
                holder.tvTotal.setText(task.diamond_selling_price)
                holder.tvPPCT.setText(task.perct_price)
            }
        }


        holder.tvINS.setText(task.diamond_status)











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
//                holder.imgPost.setImageURI(imgURI,context);
//        holder.imgUser.setImageURI(imgURI,context);
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
        return usersList.size?:0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(context)
            .inflate(R.layout.inventory_row, parent, false)
        return ViewHolder(itemView)

    }
    fun addPermission(permissionModel: PermissionModel){
        this.permissionModel=permissionModel
    }


}