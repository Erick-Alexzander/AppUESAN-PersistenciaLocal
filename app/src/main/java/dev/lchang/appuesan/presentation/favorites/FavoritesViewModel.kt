package dev.lchang.appuesan.presentation.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.lchang.appuesan.data.local.database.AppDatabase
import dev.lchang.appuesan.data.local.entity.CountryEntity
import dev.lchang.appuesan.data.model.CountryModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val countryDao = AppDatabase.getDatabase(application).countryDao()

    val favoriteCountries: StateFlow<List<CountryEntity>> = countryDao.getAllFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(country: CountryModel) {
        viewModelScope.launch {
            val isFav = favoriteCountries.value.any { it.name == country.name }
            if (isFav) {
                countryDao.deleteByName(country.name)
            } else {
                countryDao.insertFavorite(
                    CountryEntity(
                        name = country.name,
                        ranking = country.ranking,
                        imageUrl = country.imageUrl,
                        savedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun removeFavorite(entity: CountryEntity) {
        viewModelScope.launch {
            countryDao.deleteFavorite(entity)
        }
    }
}
