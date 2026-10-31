package dev.lchang.appuesan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_countries")
data class CountryEntity(
    @PrimaryKey
    val name: String,
    val ranking: Int,
    val imageUrl: String,
    val savedAt: Long = 0L
)
