package com.dsm.ui.parser

import com.dsm.ui.model.*
import com.google.gson.annotations.SerializedName

class DiamondParser : BaseModel() {

    @SerializedName("data")
    lateinit var diamondData: DiamondData

    class DiamondData{

        @SerializedName("permissions")
        lateinit var permissionModel : PermissionModel

        @SerializedName("diamonds")
        lateinit var diamonds: DiamondList

        @SerializedName("user")
        lateinit var userModel: UserModel

    }
    class DiamondList{

        @SerializedName("current_page")
        var current_page: Int = 1

        @SerializedName("from")
        var from: Int = 1

        @SerializedName("last_page")
        var last_page: Int = 1

        @SerializedName("total")
        var total: Int = 1


        @SerializedName("data")
        var data: List<DiamondModel> = emptyList()

    }

}