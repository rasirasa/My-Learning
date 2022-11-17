package com.ssrdi.co.id.myradboox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerificationRequest {
    @SerializedName("code")
    @Expose
    var code:Int?=null
}