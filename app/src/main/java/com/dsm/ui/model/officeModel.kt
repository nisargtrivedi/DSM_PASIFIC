package com.dsm.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class officeModel : Serializable {

    @SerializedName("office_id")
    var officeID: Int = 0

    @SerializedName("office_name")
    var officeName: String = ""

}