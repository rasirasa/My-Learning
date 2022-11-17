package com.ssrdi.co.id.myradboox.api

import android.widget.EditText
import com.ssrdi.co.id.myradboox.model.LoginResponse
import com.ssrdi.co.id.myradboox.model.VerificationResponse
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {

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
        @Field("code") code:Int,
        @Header("Authorization") Authorization: String?
    ): Call<VerificationResponse>
}