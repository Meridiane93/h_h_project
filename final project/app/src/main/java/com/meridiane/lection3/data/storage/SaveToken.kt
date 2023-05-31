package com.meridiane.lection3.data.storage

import android.content.Context
import com.meridiane.lection3.Constants

class SaveToken(context: Context) : TokenStorage {

    private val sharedToken =
        context.getSharedPreferences(Constants.SHARED_PREFS_TOKEN_SAVE, Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        sharedToken.edit().putString(Constants.SHARED_PREFS_KEY, token).apply()
    }

    override fun clearToken() {
       val clear =  sharedToken.edit().clear()
        clear.apply()
    }

    override fun getToken(): String =
        sharedToken.getString(Constants.SHARED_PREFS_KEY, Constants.SHARED_PREFS_DEFAULT_TOKEN)
            ?: Constants.SHARED_PREFS_DEFAULT_TOKEN




}