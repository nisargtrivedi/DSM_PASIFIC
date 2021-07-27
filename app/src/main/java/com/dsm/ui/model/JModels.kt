package com.dsm.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class JModels : Serializable {

    @SerializedName("data_id")
    var dataID : Int = 0

    @SerializedName("model_no")
    var modelNo : String = ""

    @SerializedName("document_id")
    var documentID : String = ""

    @SerializedName("price")
    var price : String = ""

    @SerializedName("image")
    var image : Image? = null




    class Image : Serializable{

        @SerializedName("document_metal_image_id")
        var imageID : String = ""

        @SerializedName("data_id")
        var dataID : String = ""

        @SerializedName("metal_id")
        var metalID : String = ""

        @SerializedName("image_path")
        var imagePath : String = ""

    }
}