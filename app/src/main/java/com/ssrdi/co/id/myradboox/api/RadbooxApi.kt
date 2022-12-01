package com.ssrdi.co.id.myradboox.api

import com.ssrdi.co.id.myradboox.model.*
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RadbooxApi {

    @FormUrlEncoded
    @POST("account/login")
    fun userLogin(
        @Field("username") username:String,
        @Field("password") password:String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("account/verification")
    fun loginVerification(
        @Field("code") code: Int,
        @Header("Authorization") Authorization: String
    ): Call<VerificationResponse>

    @GET("admin/detail")
    fun detailAdmin(
       @Header("Authorization") Authorization: String
    ): Call<DetailResponse>

    @GET("reseller/voucher")
    fun getVoucher(
        @Header("Authorization") Authorization: String
    ): Call<VoucherResponse>

    @GET("reseller/voucher/{voucher_id}")
    fun getVoucherById(
        @Header("Authorization") Authorization: String,
        @Path("voucher_id") voucherId: Int
    ): Call<VoucherResponse>

    //    @GET("reseller/voucher/")
//    fun getDetailVoucher(
//        @Header("Authorization") Authorization: String, @Query("id") id:Int
//
//    ): Call<VoucherResponseDetail>

}