package com.ssrdi.co.id.myradboox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerificationResponse {
    @SerializedName("status")
    @Expose
    var status:String?=null

    @SerializedName("token")
    @Expose
    var token:String?=null

    @SerializedName("message")
    @Expose
    var message:String?=null
}