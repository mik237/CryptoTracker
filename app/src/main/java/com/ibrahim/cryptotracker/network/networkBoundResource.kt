package com.ibrahim.cryptotracker.network

import com.ibrahim.cryptotracker.network.data.CurrencyRateResponse
import com.ibrahim.cryptotracker.utils.Constants
import kotlinx.coroutines.flow.flow
import retrofit2.Response

inline fun <T> networkBoundResource(
    crossinline isNetworkConnected: suspend () -> Boolean,
    crossinline fetch: suspend () -> Response<T>,
    crossinline insertIntoDb: suspend (data: CurrencyRateResponse) -> Unit
) = flow {


    if (isNetworkConnected()) {
        emit(Status.Loading(loading = true))
        val response = fetch()
        emit(Status.Loading(loading = false))
        if (response.isSuccessful && response.body() != null) {
            //insert into database here
            response.body()?.let {
                insertIntoDb(response.body() as CurrencyRateResponse)
            }
            emit(Status.Success)
        } else {
            emit(Status.Error(error = response.message()))
        }

    } else {
        emit(Status.Error(error = Constants.NO_INTERNET_CONNECTION))
    }
}