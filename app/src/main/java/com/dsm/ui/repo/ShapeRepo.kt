package com.dsm.ui.repo

import com.dsm.ui.network.APIInterface
import com.dsm.ui.network.RetrofitBuilder

class ShapeRepo(private val apiInterface: APIInterface) {

    suspend fun getAllShapes() = apiInterface.getAllShapes(RetrofitBuilder.CONTENT_TYPE,RetrofitBuilder.CUSTOMER_KEY,RetrofitBuilder.CUSTOMER_SECRET,RetrofitBuilder.X_API)
}