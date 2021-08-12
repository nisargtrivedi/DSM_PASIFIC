package com.dsm.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Html
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
import com.dsm.ui.listener.onJwelleryClick
import com.dsm.ui.listener.onPriceLinkClick
import com.dsm.ui.model.DiamondModel
import com.dsm.ui.model.PermissionModel
import com.dsm.ui.util.onDiamondClick


class InventoryAdapter(context: Context, usersList: List<DiamondModel>) :
    RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    var usersList: List<DiamondModel>
    var context:Context
    lateinit var permissionModel: PermissionModel

    lateinit var onDiamond: onDiamondClick
    lateinit var onPriceList: onPriceLinkClick

    public fun onClick(open: onDiamondClick){
        this.onDiamond = open
    }

    public fun onPriceClick(open: onPriceLinkClick){
        this.onPriceList = open
    }
    init {
        this.context =context
        this.usersList= usersList
    }
    lateinit var openDialog : onDialogClick

    public fun DialogOpen(open: onDialogClick){
        this.openDialog = open
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvShape: TextView = view.findViewById(R.id.tvShape)
        var tvCarat : TextView = view.findViewById(R.id.tvCarat)
        var tvColor: TextView = view.findViewById(R.id.tvColor)
        var tvClarity:TextView = view.findViewById(R.id.tvClarity)

        var tvLBH:TextView = view.findViewById(R.id.tvLBH)
        var tvCut:TextView = view.findViewById(R.id.tvCut)
        var tvFLR:TextView = view.findViewById(R.id.tvFLR)
        var tvTable:TextView = view.findViewById(R.id.tvTable)
        var tvDepth:TextView = view.findViewById(R.id.tvDepth)
        var tvGIA:TextView = view.findViewById(R.id.tvGIA)

        var tvLot:TextView = view.findViewById(R.id.tvLot)
        var tvPPCT:TextView = view.findViewById(R.id.tvPPCT)
        var tvTotal:TextView = view.findViewById(R.id.tvTotal)
        var tvINS:TextView = view.findViewById(R.id.tvINS)
        var chk: ImageView = view.findViewById(R.id.chk)
        var llImage:LinearLayout = view.findViewById(R.id.llImage)
        var llStatus : LinearLayout = view.findViewById(R.id.llStatus)

        var llPPCT : LinearLayout = view.findViewById(R.id.llPPCT)
        var llTotal : LinearLayout = view.findViewById(R.id.llTotal)
        var llCertyNo : LinearLayout = view.findViewById(R.id.llCertyNo)


    }


    override fun onBindViewHolder(
        holder: InventoryAdapter.ViewHolder,
        position: Int
    ) {
        val task: DiamondModel = usersList[position]


        holder.llImage.setOnClickListener {
            openDialog.onDialogOpen(task)
        }

        holder.chk.setOnClickListener {
            onDiamond.onClick(task)
        }
        if(task.isCheched==1){
            holder.chk.post {
                holder.chk.setImageResource(R.drawable.ic_check)
            }
        }else{
            holder.chk.post {
                holder.chk.setImageResource(R.drawable.blue_border)
            }
        }
        holder.tvShape.setText(task.diamond_shape)
        holder.tvCarat.setText(task.diamond_size)
        holder.tvColor.setText(task.diamond_clr)
        holder.tvClarity.setText(task.diamond_cla)
        holder.tvCut.setText(task.diamond_fcut)
        holder.tvLBH.setText(task.diamond_meas1 + "x" + task.diamond_meas2 + "x" + task.diamond_meas3)
        holder.tvFLR.setText(task.diamond_flr)
        holder.tvTable.setText(task.diamond_tab)
        holder.tvDepth.setText(task.diamond_dep)

        holder.tvGIA.text = (if(!task.certificate_link.isNullOrEmpty()) Html.fromHtml("<u>"+task.diamond_cert+"</u>") else task.diamond_cert)

        holder.llCertyNo.setOnClickListener {
            if(!task.certificate_link.isNullOrEmpty()) {
                val url = task.certificate_link
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(i)
            }
        }
        holder.llPPCT.setOnClickListener {
            if(permissionModel.show_pricelink) {
                onPriceList.onPriceLink(task)
            }
        }

        holder.llTotal.setOnClickListener {
            if(permissionModel.show_pricelink) {
                onPriceList.onPriceLink(task)
            }
        }

        holder.tvLot.setText(
            task.diamond_lot_no + "\n" +
                    if (permissionModel.show_location) {
                        task.location
                    } else {

                    }
        )
        if(task.diamond_status.toString().toLowerCase()=="upcoming"){
            holder.llStatus.setBackgroundColor(Color.parseColor("#5E541A"))
        }else if(task.diamond_status.toString().toLowerCase()=="new arrival"){
            holder.llStatus.setBackgroundColor(Color.parseColor("#0A8D5A"))
        }else{
            holder.llStatus.setBackgroundColor(Color.TRANSPARENT)
        }
        if(permissionModel.user_show_price){
            if(!permissionModel.show_price_aud){
                holder.tvTotal.text =  if(!permissionModel.show_pricelink) task.diamond_selling_price else { if(task.isshowPrice==0) Html.fromHtml("<u>Show Price</u>") else task.diamond_selling_price }
                holder.tvPPCT.text = if(!permissionModel.show_pricelink) task.perct_price else { if(task.isshowPrice==0) Html.fromHtml("<u>Show Price</u>") else task.perct_price }
            }else{
                holder.tvTotal.text = if(!permissionModel.show_pricelink) task.diamond_price_sell_AUD else { if(task.isshowPrice==0) Html.fromHtml("<u>Show Price</u>") else task.diamond_price_sell_AUD }
                holder.tvPPCT.text = "-"
            }
        }else{
            if(permissionModel.show_price_aud){
                holder.tvTotal.text = if(!permissionModel.show_pricelink) task.diamond_selling_price else { if(task.isshowPrice==0) Html.fromHtml("<u>Show Price</u>") else task.diamond_selling_price }
                holder.tvPPCT.text = if(!permissionModel.show_pricelink) task.perct_price else { if(task.isshowPrice==0) Html.fromHtml("<u>Show Price</u>") else task.perct_price }
            }
        }

        holder.tvINS.text = task.diamond_status











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