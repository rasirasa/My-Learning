package com.ssrdi.co.id.myradboox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequest {
    @SerializedName("username")
    @Expose
    var username:String?=null

    @SerializedName("password")
    @Expose
    var password:String?=null
}