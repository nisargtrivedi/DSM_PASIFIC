package com.dsm.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PermissionModel : Serializable{

    @SerializedName("show_price_rapnet")
    var show_price_rapnet : Boolean = false

    @SerializedName("show_price_aud")
    var show_price_aud : Boolean = false

    @SerializedName("user_show_price")
    var user_show_price : Boolean = false


    @SerializedName("show_discount")
    var show_discount : Boolean = false

    @SerializedName("show_price")
    var show_price : Boolean = false

    @SerializedName("show_pricelink")
    var show_pricelink : Boolean = false

    @SerializedName("show_location")
    var show_location : Boolean = false

    @SerializedName("show_diamond_image")
    var show_diamond_image : Boolean = false

    @SerializedName("withdraw_website_access")
    var withdraw_website_access : Boolean = false

}