package dev.lchang.appuesan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_countries")
data class ApiCountryEntity(
    @PrimaryKey
    val name: String,
    val code: String?,
    val flag: String?
)
