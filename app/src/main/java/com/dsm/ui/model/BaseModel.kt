package com.dsm.ui.model

import com.google.gson.annotations.SerializedName

open class BaseModel {

    @SerializedName("message")
    public var ResponseMessage : String =""

    @SerializedName("error")
    public var Error : String =""

    @SerializedName("status")
    public var ResponseStatus : Int =0

}