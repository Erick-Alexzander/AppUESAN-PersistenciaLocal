package dev.lchang.appuesan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.lchang.appuesan.data.local.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query("SELECT * FROM favorite_countries")
    fun getAllFavorites(): Flow<List<CountryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(country: CountryEntity)

    @Delete
    suspend fun deleteFavorite(country: CountryEntity)

    @Query("DELETE FROM favorite_countries WHERE name = :name")
    suspend fun deleteByName(name: String)
}
