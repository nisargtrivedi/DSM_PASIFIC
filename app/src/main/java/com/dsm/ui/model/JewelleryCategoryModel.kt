package com.dsm.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class JewelleryCategoryModel() : Serializable{

    @SerializedName("attribute_id")
    public var attribute_id : Int = 0

    @SerializedName("attribute_name")
    public var attribute_name : String = ""

    @SerializedName("attribute_image_path")
    public var attribute_image_path : String = ""



}
