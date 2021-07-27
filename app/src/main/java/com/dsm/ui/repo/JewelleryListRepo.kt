package com.dsm.ui.repo

import com.dsm.ui.network.APIInterface
import com.dsm.ui.network.RetrofitBuilder

class JewelleryListRepo(private val apiInterface: APIInterface) {

    suspend fun getAllJewelleryList(subcategoryID:String) = apiInterface.getAllJewelleryList(RetrofitBuilder.CONTENT_TYPE,RetrofitBuilder.CUSTOMER_KEY,RetrofitBuilder.CUSTOMER_SECRET,RetrofitBuilder.X_API,subcategoryID)
}