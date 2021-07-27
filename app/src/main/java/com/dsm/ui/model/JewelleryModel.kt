package com.dsm.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class JewelleryModel : Serializable{

    @SerializedName("jewellery_id")
    var jewelleryID : Int = 0

    @SerializedName("jewellery_front_product_name")
    var jewelleryfrontproductname : String = ""

    @SerializedName("jewellery_filename")
    var jewelleryfilename : String = ""

    @SerializedName("jewellery_lot_no")
    var jewelleryLotNo : String = ""

    @SerializedName("jewellery_front_cost_aud")
    var jewelleryfrontcostaud : String = "0"

    @SerializedName("jewellery_front_cost_usd")
    var jewelleryfrontcostusd : String = "0"


    //JDetails
    @SerializedName("jewellery_description")
    var jewellery_description : String = ""


    @SerializedName("diamond_cert")
    var diamond_cert : String = ""

    @SerializedName("gold_weight")
    var gold_weight : String = ""

    @SerializedName("jewellery_tdw")
    var jewellery_tdw : String = ""

    @SerializedName("diamond_weight")
    var diamond_weight : String = ""

    @SerializedName("jewellery_clr")
    var jewellery_clr : String = ""

    @SerializedName("jewellery_cla")
    var jewellery_cla : String = ""

    @SerializedName("jewellery_lab")
    var jewellery_lab : String = ""

    @SerializedName("jewellery_carat")
    var jewellery_carat : String = ""

    @SerializedName("jewellery_price")
    var jewellery_price : String = "0"

    @SerializedName("image")
    var image : String = ""

    @SerializedName("jewellery_metal")
    var jewellery_metal : String = ""


    @SerializedName("images")
    var images : List<Images> = emptyList()

    @SerializedName("lookup_metals")
    var metalsList : List<Metals> = emptyList()


    class Images : Serializable{

        @SerializedName("attribute_id")
        var attribute_id : Int = 0

        @SerializedName("images")
        var images : List<String> = emptyList()

    }

    class Metals : Serializable{

        @SerializedName("attribute_id")
        var metalID : Int = 0

        @SerializedName("attribute_name")
        var metalName : String = ""

    }



//    "jewellery_id": 7957,
//    "jewellery_front_product_name": "RD 0.10CT T.D.W.",
//    "jewellery_filename": "https://s3-ap-southeast-2.amazonaws.com/dsmpacific-alpha/jewellery/images/DST6.jpg",
//    "jewellery_lot_no": "DST6",
//    "jewellery_front_cost_aud": "285",
//    "jewellery_front_cost_usd": "0"
}
