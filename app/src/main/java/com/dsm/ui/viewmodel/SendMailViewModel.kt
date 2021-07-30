package com.dsm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dsm.ui.repo.MailRepo
import com.dsm.ui.repo.ShapeRepo
import com.dsm.ui.util.Resource
import kotlinx.coroutines.Dispatchers

class SendMailViewModel(private val mailRepo: MailRepo) : ViewModel() {

    fun sendMail(email:String,
                 message:String,
                 enquiryID:String,
                 send_me_also:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mailRepo.sendMail(email,message,enquiryID,send_me_also)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun sendJewelleryMail(email:String,product_name:String,
                          product_model_no:String,
                          product_metal:String,
                          product_desc:String,
                          product_price:String,
                          customer_msg:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mailRepo.sendJewelleryMail(email,product_name,product_model_no,product_metal,product_desc,product_price,customer_msg)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}