package com.ssrdi.co.id.myradboox.api

import com.ssrdi.co.id.myradboox.model.DetailResponse
import com.ssrdi.co.id.myradboox.model.LoginResponse
import com.ssrdi.co.id.myradboox.model.VerificationResponse
import com.ssrdi.co.id.myradboox.model.VoucherResponse
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
}