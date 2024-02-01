package com.test.mvvm_prac


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("iddata")
    @Expose
    val iddata: String,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("pwdata")
    @Expose
    val pwdata: String,
    @SerializedName("status")
    @Expose
    val status: String
)