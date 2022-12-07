package com.ssrdi.co.id.myradboox.storage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ssrdi.co.id.myradboox.model.DetailResponse
import com.ssrdi.co.id.myradboox.model.VerificationResponse


class SharedPrefManager private constructor(private val mCtx: Context) {

    private val preference: SharedPreferences
        get() = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor
        get() = preference.edit()

    var isLoggedIn: Boolean
        get() {
            return preference.getBoolean("isLogin", false)
        }
        set(value) {
            editor.putBoolean("isLogin", value).apply()
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


    fun saveRoleLogin(roleLogin: String) {

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

    fun saveUserPassword(userLogin: String, passLogin:String) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("userLogin", userLogin)
        editor.putString("passLogin", passLogin)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun clearAll() {
        editor.clear().apply()
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
    var tokenLogin : String
        get() = preference.getString("token","") ?:""
        set(value) {
            editor.putString("token", value).apply()
        }
    var role: String
        get() = preference.getString("role", "") ?: ""
        set(value) {
            editor.putString("role", value).apply()
        }

    var userLogin:String
        get() = preference.getString("userLogin","")?:""
        set(value) {
            editor.putString("userLogin", value).apply()
        }

    var passLogin:String
        get() = preference.getString("passLogin","")?:""
        set(value) {
            editor.putString("passLogin", value).apply()
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
