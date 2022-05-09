package com.ibrahim.cryptotracker.network.data
import com.google.gson.annotations.SerializedName


data class CurrencyRateResponse(
    @SerializedName("bpi")
    val bpi: Bpi,
    @SerializedName("chartName")
    val chartName: String,
    @SerializedName("disclaimer")
    val disclaimer: String,
    @SerializedName("time")
    val time: Time
)

data class Bpi(
    @SerializedName("EUR")
    val eUR: Currency,
    @SerializedName("GBP")
    val gBP: Currency,
    @SerializedName("USD")
    val uSD: Currency
)

data class Time(
    @SerializedName("updated")
    val updated: String?,
    @SerializedName("updatedISO")
    val updatedISO: String?,
    @SerializedName("updateduk")
    val updateduk: String?
)

data class Currency(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("rate")
    val rate: String,
    @SerializedName("rate_float")
    val rateFloat: Double,
    @SerializedName("symbol")
    val symbol: String
)