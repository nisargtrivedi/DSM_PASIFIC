package com.dsm.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class labModel : Serializable {

    @SerializedName("lab_id")
    var lab_id : Int = 0

    @SerializedName("lab_name")
    var lab_name : String = ""
}