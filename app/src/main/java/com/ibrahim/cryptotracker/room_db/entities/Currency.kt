package com.ibrahim.cryptotracker.room_db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "currency", foreignKeys = [
        ForeignKey(
            entity = Crypto::class,
            parentColumns = arrayOf("chartName"),
            childColumns = arrayOf("fk_chartName"),
            onDelete = CASCADE
        )
    ]
)
data class Currency(

    val fk_chartName: String,

    @PrimaryKey
    val code: String,
    val description: String,
    val rate: String,
    val rateFloat: Double,
    val symbol: String
)
