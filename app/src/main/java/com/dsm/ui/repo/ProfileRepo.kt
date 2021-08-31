package com.dsm.ui.repo

import com.dsm.ui.network.APIInterface
import com.dsm.ui.network.RetrofitBuilder

class ProfileRepo(private val apiInterface: APIInterface) {
    suspend fun getProfile(email:String) = apiInterface.getProfile(RetrofitBuilder.CONTENT_TYPE,RetrofitBuilder.CUSTOMER_KEY,RetrofitBuilder.CUSTOMER_SECRET,RetrofitBuilder.X_API,email)
}