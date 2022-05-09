package com.ibrahim.cryptotracker.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionManager @Inject constructor(val app: Application) {
    fun isConnected() : Boolean{
        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val network = cm.activeNetwork?:return false
            val activeNetwork = cm.getNetworkCapabilities(network)?:return false
            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                else->false
            }
        }
        else{
            @Suppress("DEPRECATION")
            val networkInfo = cm.activeNetworkInfo?:return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}