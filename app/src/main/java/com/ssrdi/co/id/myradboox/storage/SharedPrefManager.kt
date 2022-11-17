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


    fun saveToken(loginTokenResponse: VerificationResponse) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("token", loginTokenResponse.token)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun saveToken(loginTokenResponse: String) {

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
