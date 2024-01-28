package com.test.mvvm_prac

import retrofit2.Response
import retrofit2.http.GET



interface RetrofitService {
    @GET("/address/korea.json")
    suspend fun getAddressData(): Response<List<dataItem>>
}
