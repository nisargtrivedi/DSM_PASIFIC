package com.dsm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dsm.ui.repo.JewelleryCategoryRepo
import com.dsm.ui.repo.JewelleryDetailRepo
import com.dsm.ui.repo.JewellerySubCategoryRepo
import com.dsm.ui.repo.ShapeRepo
import com.dsm.ui.util.Resource
import kotlinx.coroutines.Dispatchers

class JewelleryDetailViewModel(private val jewelleryDetailRepo: JewelleryDetailRepo) : ViewModel() {

    fun getAllJewelleryDetail(jID:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = jewelleryDetailRepo.getAllJewelleryDetail(jID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}