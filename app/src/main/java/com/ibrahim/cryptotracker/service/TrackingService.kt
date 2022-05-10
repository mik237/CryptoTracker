package com.ibrahim.cryptotracker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ibrahim.cryptotracker.repo.MainRepository
import com.ibrahim.cryptotracker.room_db.entities.CryptoCurrency
import com.ibrahim.cryptotracker.utils.Constants
import com.ibrahim.cryptotracker.utils.CryptoNotificationManager
import com.ibrahim.cryptotracker.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : Service() {

    private var job: Job? = null

    @Inject lateinit var repository: MainRepository
    @Inject lateinit var preferencesManager: PreferencesManager
    @Inject lateinit var notificationManager: CryptoNotificationManager

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action: String? = intent?.action
        action?.let {
            if (it.equals(Constants.ACTION_START_TRACKING_SERVICE))
                startTrackingService()
            else if (it.equals(Constants.ACTION_STOP_TRACKING_SERVICE))
                stopTrackingService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopTrackingService() {
        job?.cancel()
        stopSelf()
    }

    private fun startTrackingService() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
           /* repository.getCryptoData().collect {
                if (it.isNotEmpty()) {
                    val cryptoCurrency = it.first()
                    if(cryptoCurrency.currencies.isNotEmpty()){
                        val currencyUSD = cryptoCurrency.currencies.first {
                            it.code == Constants.USD_CODE
                        }
                        preferencesManager.getLimit(Constants.MAX_LIMIT_KEY)?.let { maxLimit ->
                            if (currencyUSD.rateFloat >= maxLimit.toDouble()) {
                                notificationManager.notifyUser(
                                    "Max Limit",
                                    "${currencyUSD.description} rate has reached to max limit $maxLimit",
                                    Constants.MAX_NOTIFICATION_ID,
                                    this@TrackingService
                                )
                            }
                        }

                        preferencesManager.getLimit(Constants.MIN_LIMIT_KEY)?.let { minLimit ->
                            if (currencyUSD.rateFloat <= minLimit.toDouble()) {

                                notificationManager.notifyUser(
                                    "Min Limit",
                                    "${currencyUSD.description} rate has dropped to min limit $minLimit",
                                    Constants.MIN_NOTIFICATION_ID,
                                    this@TrackingService
                                )
                            }
                        }
                    }
                }

            }*/
            var i = 0
            while (true) {
                Log.d("_TAG_", "Tracking: ${i++}")
                repository.fetchCurrencyRate().collect {}
                delay(6000L)
            }
        }
    }
}