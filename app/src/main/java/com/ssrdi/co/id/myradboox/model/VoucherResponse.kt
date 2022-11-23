package com.ssrdi.co.id.myradboox.model

import android.util.Size
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class VoucherResponse(
    val status:String,
    val message:String,
    val data:List<VoucherItemResponse>
)

data class VoucherItemResponse(
    val id:Int,
    val username:String,
    val password:String,
    val profile:String
)

//class VoucherResponse {
////    @SerializedName("status")
////    @Expose
////    var status:String?=null
////
////    @SerializedName("message")
////    @Expose
////    var message:String?=null
//
//    @SerializedName("data")
//    @Expose
//    var data: DataObject?=null
//
//    class DataObject{
//
//        @SerializedName("id")
//        @Expose
//        var id:Int?=null
//
//        @SerializedName("username")
//        @Expose
//        var username:String?=null
//
//        @SerializedName("password")
//        @Expose
//        var password:String?=null
//
//        @SerializedName("profile")
//        @Expose
//        var profile:String?=null
//
//        @SerializedName("nas")
//        @Expose
//        var nas:String?=null
//
//        @SerializedName("server")
//        @Expose
//        var server:String?=null
//
//        @SerializedName("status")
//        @Expose
//        var status:String?=null
//
//        @SerializedName("create_time")
//        @Expose
//        var create_time:String?=null
//
//        @SerializedName("start_time")
//        @Expose
//        var start_time:String?=null
//
//        @SerializedName("end_time")
//        @Expose
//        var end_time:String?=null
//    }
//
//}