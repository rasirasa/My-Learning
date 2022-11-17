package com.ssrdi.co.id.myradboox.storage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ssrdi.co.id.myradboox.model.DetailResponse
import com.ssrdi.co.id.myradboox.model.VerificationResponse


class SharedPrefManager private constructor(private val mCtx: Context){
    val isLoggedIn: Boolean
        get() {

            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id", -1) != -1
        }

//    val userVerification: VerificationResponse
//        get() {
//            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
//            return userVerification(
//                sharedPreferences.getInt("id", -1),
//                sharedPreferences.getString("email", null),
//                sharedPreferences.getString("name", null),
//                sharedPreferences.getString("school", null)
//            )
//        }


    fun saveRoleLogin(roleLogin:String){

            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("role", roleLogin)
    }


    fun saveToken(loginTokenResponse: String) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("token", loginTokenResponse)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun saveDetailAdmin(loginTokenResponse: DetailResponse) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("status", loginTokenResponse.status)
        editor.putString("message", loginTokenResponse.message)
        editor.putString("username", loginTokenResponse.username)
        editor.putString("role", loginTokenResponse.role.toString())
        editor.putString("name", loginTokenResponse.name)
        editor.putString("phone", loginTokenResponse.phone)
        editor.putString("email", loginTokenResponse.email)
        editor.putString("url_img", loginTokenResponse.url_img)
        editor.putString("address", loginTokenResponse.address)
        editor.putString("city", loginTokenResponse.city)
        editor.putString("code", loginTokenResponse.code)
        editor.putString("country", loginTokenResponse.country)

        editor.apply()

    }

    fun getRole(v_role:String){
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("role", v_role)
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }
}
