package com.test.mvvm_prac


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class dataItem(
    @SerializedName("en")
    @Expose
    val en: String,
    @SerializedName("ko")
    @Expose
    val ko: String
)