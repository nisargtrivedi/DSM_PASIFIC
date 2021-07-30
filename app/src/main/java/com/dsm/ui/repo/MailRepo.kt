package com.dsm.ui.repo

import com.dsm.ui.network.APIInterface
import com.dsm.ui.network.RetrofitBuilder
import retrofit2.http.Field

class MailRepo(private val apiInterface: APIInterface) {

    suspend fun sendMail(email:String,
                         message:String,
                         enquiryID:String,
                         send_me_also:String) =
        apiInterface.sendMail(
            RetrofitBuilder.CUSTOMER_KEY,
            RetrofitBuilder.CUSTOMER_SECRET,
            RetrofitBuilder.X_API,
            email,
            message,
            enquiryID,
            send_me_also,
            "Certified"
        )

    suspend fun sendJewelleryMail(email:String,product_name:String,
                                  product_model_no:String,
                                  product_metal:String,
                                  product_desc:String,
                                   product_price:String,
                                 customer_msg:String) =
        apiInterface.jewelleryEnquiry(
            RetrofitBuilder.CUSTOMER_KEY,
            RetrofitBuilder.CUSTOMER_SECRET,
            RetrofitBuilder.X_API,
            email,
            product_name,
            product_model_no,
            product_metal,
             product_desc,
            product_price,
            customer_msg
        )
}