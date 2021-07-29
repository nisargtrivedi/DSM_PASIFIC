package com.dsm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dsm.ui.repo.DiamondRepo
import com.dsm.ui.util.Resource
import kotlinx.coroutines.Dispatchers

class DiamondViewModel(private val diamondRepo: DiamondRepo) : ViewModel() {

    fun getAllDiamonds(
        email:String, shape:String, page:Int,
        query: String?,
        srch_carat: String?,
        srch_status:String?,
        srch_dia_shape:String?,
        srch_lab:String?,
        srch_dia_clr:String?,
        srch_dia_cla:String?,
        srch_price:String?,
        srch_dia_fcut:String?,
        srch_dia_pol:String?,
        srch_dia_sym:String?
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = diamondRepo.getAllDiamond(email,shape,page,query,srch_carat,srch_status,srch_dia_shape,srch_lab,srch_dia_clr,srch_dia_cla,
            srch_price,srch_dia_fcut,srch_dia_pol,srch_dia_sym)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}