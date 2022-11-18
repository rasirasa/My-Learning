package com.ssrdi.co.id.myradboox

import android.content.Context
import android.content.Intent

object UserRoleUtils {
    fun checkUserRole(ctx: Context, role:String){
        val intent : Intent = when (role){
            "1" -> {
                Intent(ctx, AdminActivity::class.java)
            }
            "2" -> {
                Intent(ctx, OperatorActivity::class.java)
            }
            "3" -> {
                Intent(ctx, KasirActivity::class.java)
            }
            "4" -> {
                Intent(ctx, ResellerActivity::class.java)
            } else -> {
                Intent(ctx, LoginActivity::class.java)
            }
        }
        ctx.startActivity(intent)
    }
}