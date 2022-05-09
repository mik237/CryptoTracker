package com.ibrahim.cryptotracker.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import javax.inject.Inject



class PreferencesManager @Inject constructor(app : Application){

    private val sharedPreferences : SharedPreferences = app.getSharedPreferences("", Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = sharedPreferences.edit()

    init {
        editor.apply()
    }


    fun setString(key: String, value: String){
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String) = sharedPreferences.getString(key, "")
    fun getLimit(key: String) = sharedPreferences.getString(key, "0.0")

}