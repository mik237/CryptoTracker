package com.ibrahim.cryptotracker.network

sealed class Status{
    object Success : Status()
    data class Loading(val loading: Boolean) : Status()
    data class Error(val error: String) : Status()
}