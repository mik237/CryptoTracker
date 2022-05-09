package com.ibrahim.cryptotracker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ibrahim.cryptotracker.repo.MainRepository
import com.ibrahim.cryptotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : Service() {

    private var job: Job? = null

    @Inject
    lateinit var repository: MainRepository

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action : String? = intent?.action
        action?.let {
            if(it.equals(Constants.ACTION_START_TRACKING_SERVICE))
                startTrackingService()
            else if(it.equals(Constants.ACTION_STOP_TRACKING_SERVICE))
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
       job =  CoroutineScope(Dispatchers.IO).launch {
            while (true){
                 repository.fetchCurrencyRate().collect {  }
                delay(60000L)
            }
        }
    }
}