package com.dsm.ui.parser

import com.dsm.ui.model.BaseModel
import com.dsm.ui.model.ShapeModel
import com.google.gson.annotations.SerializedName

class ShapeParser : BaseModel() {

    @SerializedName("data")
    lateinit var list : ShapeList

    class ShapeList{
        @SerializedName("shapes")
        var list : List<ShapeModel> = emptyList()
    }
}