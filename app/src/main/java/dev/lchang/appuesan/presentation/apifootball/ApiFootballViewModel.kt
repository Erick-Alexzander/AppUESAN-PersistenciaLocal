package dev.lchang.appuesan.presentation.apifootball

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.lchang.appuesan.data.local.database.AppDatabase
import dev.lchang.appuesan.data.local.entity.ApiCountryEntity
import dev.lchang.appuesan.data.remote.apifootball.Country
import dev.lchang.appuesan.data.remote.apifootball.RetrofitInstance
import dev.lchang.appuesan.data.remote.apifootball.TeamWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ApiFootballViewModel(application: Application): AndroidViewModel(application){

    private val apiCountryDao = AppDatabase.getDatabase(application).apiCountryDao()

    val savedCountries: StateFlow<List<ApiCountryEntity>> = apiCountryDao.getAllCountries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedCountry = MutableStateFlow<ApiCountryEntity?>(null)
    val selectedCountry: StateFlow<ApiCountryEntity?> = _selectedCountry
    private val _teams = MutableStateFlow<List<TeamWrapper>>(emptyList())
    val teams: StateFlow<List<TeamWrapper>> = _teams
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _errorMessages = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _errorMessages

    fun fetchAndSaveCountries(){
        viewModelScope.launch {
            _isLoading.value  = true
            _errorMessages.value = null
            try {
                val response = RetrofitInstance.api.getCountries()
                val entities = response.response.map { 
                    ApiCountryEntity(it.name, it.code, it.flag)
                }
                apiCountryDao.insertAll(entities)
            }catch (e: Exception){
                _errorMessages.value = "Error al consultar la API: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTeamsByCountry(country: ApiCountryEntity){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getTeamsByCountry(country.name)
                _teams.value = response.response
                _errorMessages.value = null
                _selectedCountry.value = country
            }catch (e: Exception){
                _errorMessages.value = "Error al cargar equipos: ${e.message}"
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun onCountrySelected(country: ApiCountryEntity){
        _selectedCountry.value = country
        loadTeamsByCountry(country)
    }
}
