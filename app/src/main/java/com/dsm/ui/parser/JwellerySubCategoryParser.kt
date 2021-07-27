package com.dsm.ui.parser

import com.dsm.ui.model.BaseModel
import com.dsm.ui.model.JewelleryCategoryModel
import com.dsm.ui.model.ShapeModel
import com.google.gson.annotations.SerializedName

class JwellerySubCategoryParser : BaseModel() {

    @SerializedName("data")
    lateinit var list : CategoryList

    class CategoryList{
        @SerializedName("sub_categories")
        var list : List<JewelleryCategoryModel> = emptyList()
    }
}