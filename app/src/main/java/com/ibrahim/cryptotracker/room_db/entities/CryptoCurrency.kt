package com.ibrahim.cryptotracker.room_db.entities

import androidx.room.Embedded
import androidx.room.Relation


data class CryptoCurrency(
    @Embedded
    val crypto: Crypto,
    @Relation(
        parentColumn = "chartName",
        entityColumn = "fk_chartName"
    )
    val currencies: List<Currency>
)
