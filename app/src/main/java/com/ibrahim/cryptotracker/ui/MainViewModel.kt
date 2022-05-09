package com.ibrahim.cryptotracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahim.cryptotracker.network.Status
import com.ibrahim.cryptotracker.repo.MainRepository
import com.ibrahim.cryptotracker.room_db.entities.CryptoCurrency
import com.ibrahim.cryptotracker.utils.Constants
import com.ibrahim.cryptotracker.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {


    private val _fetchFromNetworkLiveData: MutableLiveData<Status> by lazy { MutableLiveData<Status>() }
    val fetchFromNetworkLiveData: LiveData<Status> = _fetchFromNetworkLiveData

    private val _fetchFromDbLiveData: MutableLiveData<CryptoCurrency> by lazy { MutableLiveData<CryptoCurrency>() }
    val fetchFromDbLiveData: LiveData<CryptoCurrency> = _fetchFromDbLiveData

    init {
        fetchCurrencyRate()
    }


    private fun fetchCurrencyRate() {
        viewModelScope.launch {
            //fetching from network
            mainRepository.fetchCurrencyRate().collect {
                _fetchFromNetworkLiveData.value = it
            }

            //get data from room database
            mainRepository.getCryptoData().collect {
                if (it.isNotEmpty())
                    _fetchFromDbLiveData.value = it.first()
            }
        }
    }

    fun saveLimit(limit: String, key: String) = preferencesManager.setString(key, limit)
    fun getLimit(key: String): String = preferencesManager.getLimit(key) ?: "0.0"

}