package com.ssrdi.co.id.myradboox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailResponse {
    @SerializedName("status")
    @Expose
    var status:String?=null

    @SerializedName("message")
    @Expose
    var message:String?=null

    @SerializedName("username")
    @Expose
    var username:String?=null

    @SerializedName("role")
    @Expose
    var role:Int?=null

    @SerializedName("name")
    @Expose
    var name:String?=null

    @SerializedName("phone")
    @Expose
    var phone:String?=null

    @SerializedName("email")
    @Expose
    var email:String?=null

    @SerializedName("url_img")
    @Expose
    var url_img:String?=null

    @SerializedName("address")
    @Expose
    var address:String?=null

    @SerializedName("city")
    @Expose
    var city:String?=null

    @SerializedName("code")
    @Expose
    var code:String?=null

    @SerializedName("country")
    @Expose
    var country:String?=null


}