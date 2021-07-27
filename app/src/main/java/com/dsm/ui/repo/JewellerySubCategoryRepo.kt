package com.dsm.ui.repo

import com.dsm.ui.network.APIInterface
import com.dsm.ui.network.RetrofitBuilder

class JewellerySubCategoryRepo(private val apiInterface: APIInterface) {

    suspend fun getAllJewellerySubCategory(categoryID:String) = apiInterface.getAllJewellerySubCategory(RetrofitBuilder.CONTENT_TYPE,RetrofitBuilder.CUSTOMER_KEY,RetrofitBuilder.CUSTOMER_SECRET,RetrofitBuilder.X_API,categoryID)
}