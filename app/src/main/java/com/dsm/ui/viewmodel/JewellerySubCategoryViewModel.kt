package com.dsm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dsm.ui.repo.JewelleryCategoryRepo
import com.dsm.ui.repo.JewellerySubCategoryRepo
import com.dsm.ui.repo.ShapeRepo
import com.dsm.ui.util.Resource
import kotlinx.coroutines.Dispatchers

class JewellerySubCategoryViewModel(private val jewellerySubCategoryRepo: JewellerySubCategoryRepo) : ViewModel() {

    fun getAllJewellerySubCategory(categoryID:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = jewellerySubCategoryRepo.getAllJewellerySubCategory(categoryID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}