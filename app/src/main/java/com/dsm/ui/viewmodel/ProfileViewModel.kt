package com.dsm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dsm.ui.repo.ProfileRepo
import com.dsm.ui.repo.ShapeRepo
import com.dsm.ui.util.Resource
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(private val profileRepo: ProfileRepo) : ViewModel() {

    fun getProfile(email:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = profileRepo.getProfile(email)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}