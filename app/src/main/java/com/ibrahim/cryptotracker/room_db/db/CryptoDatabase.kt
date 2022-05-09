package com.ibrahim.cryptotracker.room_db.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ibrahim.cryptotracker.room_db.dao.CryptoDao
import com.ibrahim.cryptotracker.room_db.entities.Crypto
import com.ibrahim.cryptotracker.room_db.entities.Currency
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(Crypto::class, Currency::class), version = 1)
abstract class CryptoDatabase : RoomDatabase(){

    abstract fun cryptoDao() : CryptoDao


    companion object{
        private var INSTANCE : CryptoDatabase?=null

        fun getDatabase(context: Context):CryptoDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    CryptoDatabase::class.java,
                    "crypto_database")
                    .build()
                INSTANCE=instance
                instance
            }
        }
    }
}