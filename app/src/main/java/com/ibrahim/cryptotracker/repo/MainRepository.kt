package com.ibrahim.cryptotracker.repo

import com.ibrahim.cryptotracker.network.NetworkService
import com.ibrahim.cryptotracker.network.networkBoundResource
import com.ibrahim.cryptotracker.room_db.db.CryptoDatabase
import com.ibrahim.cryptotracker.room_db.entities.Crypto
import com.ibrahim.cryptotracker.room_db.entities.Currency
import com.ibrahim.cryptotracker.utils.ConnectionManager
import com.ibrahim.cryptotracker.utils.Constants
import com.ibrahim.cryptotracker.utils.NotificationManager
import com.ibrahim.cryptotracker.utils.PreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val connectionManager: ConnectionManager,
    private val networkService: NetworkService,
    private val cryptoDatabase: CryptoDatabase,
    private val notificationManager: NotificationManager,
    private val preferencesManager: PreferencesManager
) {

    fun fetchCurrencyRate() = networkBoundResource(
        isNetworkConnected = { connectionManager.isConnected() },
        fetch = {
            networkService.getCurrencyRate()
        },
        insertIntoDb = {
            val crypto = Crypto(
                chartName = it.chartName,
                disclaimer = it.disclaimer,
                updated = it.time.updated ?: "",
                updatedISO = it.time.updatedISO ?: "",
                updateduk = it.time.updateduk ?: ""
            )
            cryptoDatabase.cryptoDao().insertCrypto(crypto)
            val currencyGBP = Currency(
                fk_chartName = it.chartName,
                code = it.bpi.gBP.code,
                description = it.bpi.gBP.description,
                rate = it.bpi.gBP.rate,
                rateFloat = it.bpi.gBP.rateFloat,
                symbol = it.bpi.gBP.symbol
            )
            val currencyEUR = Currency(
                fk_chartName = it.chartName,
                code = it.bpi.eUR.code,
                description = it.bpi.eUR.description,
                rate = it.bpi.eUR.rate,
                rateFloat = it.bpi.eUR.rateFloat,
                symbol = it.bpi.eUR.symbol
            )
            val currencyUSD = Currency(
                fk_chartName = it.chartName,
                code = it.bpi.uSD.code,
                description = it.bpi.uSD.description,
                rate = it.bpi.uSD.rate,
                rateFloat = it.bpi.uSD.rateFloat,
                symbol = it.bpi.uSD.symbol
            )

            preferencesManager.getLimit(Constants.MAX_LIMIT_KEY)?.let { maxLimit ->
                if (currencyUSD.rateFloat >= maxLimit.toDouble()) {
                    notificationManager.notifyUser(
                        "Max Limit",
                        "${currencyUSD.description} rate has reached to max limit $maxLimit",
                        Constants.MAX_NOTIFICATION_ID
                    )
                }
            }

            preferencesManager.getLimit(Constants.MIN_LIMIT_KEY)?.let { minLimit ->
                if (currencyUSD.rateFloat <= minLimit.toDouble()) {
                    notificationManager.notifyUser(
                        "Min Limit",
                        "${currencyUSD.description} rate has dropped to min limit $minLimit",
                        Constants.MIN_NOTIFICATION_ID
                    )
                }
            }

            val currencies = listOf(currencyEUR, currencyGBP, currencyUSD)
            cryptoDatabase.cryptoDao().insertCurrency(currencies)
        }
    )

    fun getCryptoData() = cryptoDatabase.cryptoDao().getCryptoList()
}