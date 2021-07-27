package com.dsm.ui.network

import com.dsm.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    const val CONTENT_TYPE = "multipart/form-data"
    const val CUSTOMER_KEY = "98f13708210194c475687be6106a3b84"
    const val CUSTOMER_SECRET = "fb191b5a87e994fc286b26efeef99c68ea71d461a828212df76b5515c43e8d5d"
    const val X_API = "8yZpsgTmPr8RS0xIO9Ebb66bzpjdn3Ev4kwh9ZKD"

    private const val BASE_URL = "https://q0n5q0f88c.execute-api.ap-southeast-2.amazonaws.com/Beta/"
    //private const val BASE_URL = "https://howtodoandroid.com/"
        //private const val BASE_URL = BuildConfig.API_URL

    private fun getRetrofit(): Retrofit {

        //Start Region
        //For log in Logcat
        val interceptor = Interceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder().addHeader("Cache-Control", "no-cache")
            request = builder.build()
            chain.proceed(request)
        }
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        if(BuildConfig.DEBUG)
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        else
            logging.setLevel(HttpLoggingInterceptor.Level.NONE)

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)
        client.addInterceptor(interceptor)
        client.connectTimeout(60, TimeUnit.SECONDS)
        client.readTimeout(60, TimeUnit.SECONDS)
        //End Region

        return Retrofit.Builder()
            .client(client.build())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: APIInterface = getRetrofit().create(APIInterface::class.java)
}