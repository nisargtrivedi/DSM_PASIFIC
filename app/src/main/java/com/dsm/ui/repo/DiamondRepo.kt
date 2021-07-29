package com.dsm.ui.repo

import com.dsm.ui.network.APIInterface
import com.dsm.ui.network.RetrofitBuilder

class DiamondRepo(private val apiInterface: APIInterface) {

//    suspend fun getAllDiamond(email:String,shapeID:String,page:Int) = apiInterface.getDiamondsByShapeID(RetrofitBuilder.CONTENT_TYPE,RetrofitBuilder.CUSTOMER_KEY,RetrofitBuilder.CUSTOMER_SECRET,RetrofitBuilder.X_API,email,shapeID,page)
    suspend fun getAllDiamond(email:String,shapeID:String,page:Int,
                              query:String?,
                              srch_carat:String?,
                              srch_status:String?,
                              srch_dia_shape:String?,
                              srch_lab:String?,
                              srch_dia_clr:String?,
                              srch_dia_cla:String?,
                              srch_price:String?,
                              srch_dia_fcut:String?,
                              srch_dia_pol:String?,
                              srch_dia_sym:String?
) = apiInterface.getDiamondsByShapeID(
    RetrofitBuilder.CUSTOMER_KEY,RetrofitBuilder.CUSTOMER_SECRET,RetrofitBuilder.X_API,
    email,shapeID,page,query,srch_carat,srch_status,srch_dia_shape,srch_lab,srch_dia_clr,srch_dia_cla,srch_price,srch_dia_fcut,srch_dia_pol,srch_dia_sym)
}