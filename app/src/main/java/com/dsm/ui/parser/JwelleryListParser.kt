package com.dsm.ui.parser

import com.dsm.ui.model.*
import com.google.gson.annotations.SerializedName

class JwelleryListParser : BaseModel() {

    @SerializedName("data")
    lateinit var list : JewelleryList

    class JewelleryList{
        @SerializedName("jewelleries")
        var list : List<JewelleryModel> = emptyList()

        @SerializedName("models")
        var listModel : List<JModels> = emptyList()
    }
}