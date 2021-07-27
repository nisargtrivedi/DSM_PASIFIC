package com.dsm.ui.repo

import com.dsm.ui.network.APIInterface
import com.dsm.ui.network.RetrofitBuilder

class JewelleryDetailRepo(private val apiInterface: APIInterface) {

    suspend fun getAllJewelleryDetail(jID:String) = apiInterface.getAllJewelleryDetail(RetrofitBuilder.CONTENT_TYPE,RetrofitBuilder.CUSTOMER_KEY,RetrofitBuilder.CUSTOMER_SECRET,RetrofitBuilder.X_API,jID)
}