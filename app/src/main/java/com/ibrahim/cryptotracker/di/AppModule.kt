package com.ibrahim.cryptotracker.di

import android.app.Application
import com.ibrahim.cryptotracker.room_db.db.CryptoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.ibrahim.cryptotracker.utils.PreferencesManager


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providePreferences(app : Application) : PreferencesManager = PreferencesManager(app)

    @Singleton
    @Provides
    fun getCryptoDatabase(app: Application): CryptoDatabase= CryptoDatabase.getDatabase(app)
}