package com.dsm.ui.parser

import com.dsm.ui.model.BaseModel
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.ShapeModel
import com.google.gson.annotations.SerializedName

class JwelleryCategoryParser : BaseModel() {

    @SerializedName("data")
    lateinit var list : CategoryList

    class CategoryList{
        @SerializedName("categories")
        var list : List<JewelleryCategoryModel> = emptyList()
    }
}