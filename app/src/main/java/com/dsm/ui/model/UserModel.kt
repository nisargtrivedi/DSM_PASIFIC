package com.dsm.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserModel : Serializable {

    @SerializedName("user_id")
    var user_id : String = ""

    @SerializedName("user_first_name")
    var user_first_name : String = ""

    @SerializedName("company_name")
    var company_name : String = ""

}