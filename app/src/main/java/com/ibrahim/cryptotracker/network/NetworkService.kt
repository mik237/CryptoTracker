package com.ibrahim.cryptotracker.network

import com.ibrahim.cryptotracker.network.data.CurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("currentprice.json")
    suspend fun getCurrencyRate():Response<CurrencyRateResponse>
}