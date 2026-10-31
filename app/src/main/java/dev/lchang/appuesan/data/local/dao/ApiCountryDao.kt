package dev.lchang.appuesan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.lchang.appuesan.data.local.entity.ApiCountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiCountryDao {
    @Query("SELECT * FROM api_countries ORDER BY name ASC")
    fun getAllCountries(): Flow<List<ApiCountryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<ApiCountryEntity>)
}
