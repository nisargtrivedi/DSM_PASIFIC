package com.dsm.ui.repo

import com.dsm.ui.network.APIInterface
import com.dsm.ui.network.RetrofitBuilder

class JewelleryCategoryRepo(private val apiInterface: APIInterface) {

    suspend fun getAllJewelleryCategory(email:String) = apiInterface.getAllJewelleryCategory(RetrofitBuilder.CONTENT_TYPE,RetrofitBuilder.CUSTOMER_KEY,RetrofitBuilder.CUSTOMER_SECRET,RetrofitBuilder.X_API,email)
}