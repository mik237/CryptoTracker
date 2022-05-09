package com.ibrahim.cryptotracker.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ibrahim.cryptotracker.room_db.entities.Crypto
import com.ibrahim.cryptotracker.room_db.entities.CryptoCurrency
import com.ibrahim.cryptotracker.room_db.entities.Currency
import kotlinx.coroutines.flow.Flow


@Dao
interface CryptoDao {

    @Query("SELECT * FROM crypto")
    fun getCryptoList(): Flow<List<CryptoCurrency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrypto(crypto: Crypto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: List<Currency>)
}