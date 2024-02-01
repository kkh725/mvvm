package com.test.mvvm_prac

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface RetrofitService {
    @FormUrlEncoded
    @POST("return_jsonobjec.jsp")
    suspend fun getAddressData(@Field("Id") Id : String,
                               @Field("Pw") Pw : String): Response<Item>
}
