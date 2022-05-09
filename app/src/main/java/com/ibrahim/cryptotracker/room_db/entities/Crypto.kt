package com.ibrahim.cryptotracker.room_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "crypto")
data class Crypto(
    @PrimaryKey
    val chartName: String,
    val disclaimer: String,
    val updated: String,
    val updatedISO: String,
    val updateduk: String
    )
