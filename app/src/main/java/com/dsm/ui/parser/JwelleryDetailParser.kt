package com.dsm.ui.parser

import com.dsm.ui.model.*
import com.google.gson.annotations.SerializedName

class JwelleryDetailParser : BaseModel() {

    @SerializedName("data")
    lateinit var list : JewelleryList


    class JewelleryList{
        @SerializedName("is_jewellery")
        var isJewellery : Boolean = true

        @SerializedName("model")
        var listModel :JewelleryModel? = null

        @SerializedName("jewellery")
        var jObj : JewelleryModel? = null
    }
}