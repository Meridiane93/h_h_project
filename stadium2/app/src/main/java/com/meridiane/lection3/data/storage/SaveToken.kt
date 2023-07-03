package com.meridiane.lection3.data.storage

import android.content.Context
import com.meridiane.lection3.Constants

class SaveToken(context: Context) : TokenStorage, CoordinateStorage {

    private val sharedToken =
        context.getSharedPreferences(Constants.SHARED_PREFS_TOKEN_SAVE, Context.MODE_PRIVATE)

    private val sharedCoordinate =
        context.getSharedPreferences(Constants.SHARED_PREFS_COORDINATE_SAVE, Context.MODE_PRIVATE)



    override fun saveToken(token: String) =
        sharedToken.edit().putString(Constants.SHARED_PREFS_KEY, token).apply()

    override fun clearToken() =
        sharedToken.edit().clear().apply()

    override fun getToken(): String =
        sharedToken.getString(Constants.SHARED_PREFS_KEY, Constants.SHARED_PREFS_DEFAULT_TOKEN)
            ?: Constants.SHARED_PREFS_DEFAULT_TOKEN


    override fun getCoordinate(): String =
        sharedCoordinate.getString(
            Constants.SHARED_PREFS_KEY_COORDINATE,
            Constants.SHARED_PREFS_DEFAULT_Coordinate
        )
            ?: Constants.SHARED_PREFS_DEFAULT_Coordinate

    override fun saveCoordinate(coordinate: String) =
        sharedCoordinate.edit().putString(Constants.SHARED_PREFS_KEY_COORDINATE, coordinate).apply()

    override fun clearCoordinate() =
        sharedCoordinate.edit().clear().apply()

}