package com.dsm.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DiamondModel : Serializable{

    @SerializedName("diamond_id")
    var diamond_id : String  = ""

    @SerializedName("diamond_type")
    var diamond_type : String = ""

    @SerializedName("diamond_lot_no")
    var diamond_lot_no : String = ""

    @SerializedName("diamond_meas1")
    var diamond_meas1 : String = ""

    @SerializedName("diamond_meas2")
    var diamond_meas2 : String = ""

    @SerializedName("diamond_meas3")
    var diamond_meas3 : String = ""

    @SerializedName("diamond_dep")
    var diamond_dep : String = ""

    @SerializedName("diamond_tab")
    var diamond_tab : String = ""

    @SerializedName("diamond_cert")
    var diamond_cert : String = ""

    @SerializedName("diamond_size")
    var diamond_size : String = ""

    @SerializedName("diamond_status")
    var diamond_status : String = ""

    @SerializedName("diamond_img_path")
    var diamond_img_path : String = ""

    @SerializedName("diamond_clr")
    var diamond_clr : String = ""

    @SerializedName("diamond_cla")
    var diamond_cla : String = ""

    @SerializedName("diamond_fcut")
    var diamond_fcut : String = ""

    @SerializedName("diamond_pol")
    var diamond_pol : String = ""

    @SerializedName("diamond_sym")
    var diamond_sym : String = ""


    @SerializedName("diamond_flr")
    var diamond_flr : String = ""

    @SerializedName("location")
    var location : String = ""

    @SerializedName("diamond_shape")
    var diamond_shape : String = ""

    @SerializedName("diamond_lab")
    var diamond_lab : String = ""

    @SerializedName("diamond_price_rapnet_final")
    var diamond_price_rapnet_final : String = ""

    @SerializedName("diamond_discount_final")
    var diamond_discount_final : String = ""

    @SerializedName("diamond_price_sell_AUD")
    var diamond_price_sell_AUD : String = ""

    @SerializedName("perct_price")
    var perct_price : String = ""

    @SerializedName("diamond_selling_price")
    var diamond_selling_price : String = ""

    @SerializedName("certificate_link")
    var certificate_link : String = ""

}