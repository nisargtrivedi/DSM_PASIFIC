package com.dsm.ui.model

import java.io.Serializable

data class InventoryModel(var isCheched : Int,
var shape : String, var carat : String,var color : String,var clarity : String,var cut : String,
                          var lbh : String,var FLR : String,var table : String,var depth : String,
                          var GIA : String,var lotNo : String,var PPCT : String,var total : String,
                          var INS : String
                          ) : Serializable
