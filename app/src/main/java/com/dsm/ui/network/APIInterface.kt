package com.dsm.ui.network


import com.dsm.ui.model.BaseModel
import com.dsm.ui.parser.*
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    //Diamond API
    @GET("shapes")
    suspend fun getAllShapes(
        @Header("Content-Type") contentType : String,
        @Header("customer-key") customerKey : String,
        @Header("customer-secret") customerSecret : String,
        @Header("x-api-key") xApiKey:String
    ) : ShapeParser

    @FormUrlEncoded
    @POST("diamonds")
    suspend fun getDiamondsByShapeID(
        @Header("customer-key") customerKey : String,
        @Header("customer-secret") customerSecret : String,
        @Header("x-api-key") xApiKey:String,
        @Field("email_id") email:String,
        @Field("shape_id") shape_id:String,
        @Field("page") page:Int,
        @Field("query") query:String?,
        @Field("srch_carat") srch_carat:String?,
        @Field("srch_status") srch_status:String?,
        @Field("srch_dia_shape") srch_dia_shape:String?,
        @Field("srch_lab") srch_lab:String?,
        @Field("srch_dia_clr") srch_dia_clr:String?,
        @Field("srch_dia_cla") srch_dia_cla:String?,
        @Field("srch_price") srch_price:String?
        ) : DiamondParser

    //End Diamond

    //Jewellery API
    @GET("jewellery/categories")
    suspend fun getAllJewelleryCategory(
        @Header("Content-Type") contentType : String,
        @Header("customer-key") customerKey : String,
        @Header("customer-secret") customerSecret : String,
        @Header("x-api-key") xApiKey:String
    ) : JwelleryCategoryParser


    //Jewellery API
    @GET("jewellery/sub-categories")
    suspend fun getAllJewellerySubCategory(
        @Header("Content-Type") contentType : String,
        @Header("customer-key") customerKey : String,
        @Header("customer-secret") customerSecret : String,
        @Header("x-api-key") xApiKey:String,
        @Query("attribute_id") categoryID:String
    ) : JwellerySubCategoryParser

    //Jewellery API
    @GET("jewellery/listing")
    suspend fun getAllJewelleryList(
        @Header("Content-Type") contentType : String,
        @Header("customer-key") customerKey : String,
        @Header("customer-secret") customerSecret : String,
        @Header("x-api-key") xApiKey:String,
        @Query("attribute_id") subcategoryID:String
    ) : JwelleryListParser

    //Jewellery API
    @GET("jewellery")
    suspend fun getAllJewelleryDetail(
            @Header("Content-Type") contentType : String,
            @Header("customer-key") customerKey : String,
            @Header("customer-secret") customerSecret : String,
            @Header("x-api-key") xApiKey:String,
            @Query("jewellery_id") jID:String
    ) : JwelleryDetailParser
}