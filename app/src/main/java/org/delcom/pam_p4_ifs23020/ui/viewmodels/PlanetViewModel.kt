package org.delcom.pam_p4_ifs23020.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanetData
import org.delcom.pam_p4_ifs23020.network.planets.service.IPlanetRepository
import javax.inject.Inject

// UI States
sealed interface PlanetsListUIState {
    data class Success(val data: List<ResponsePlanetData>) : PlanetsListUIState
    data class Error(val message: String) : PlanetsListUIState
    object Loading : PlanetsListUIState
}

sealed interface PlanetDetailUIState {
    data class Success(val data: ResponsePlanetData) : PlanetDetailUIState
    data class Error(val message: String) : PlanetDetailUIState
    object Loading : PlanetDetailUIState
}

sealed interface PlanetActionUIState {
    data class Success(val message: String) : PlanetActionUIState
    data class Error(val message: String) : PlanetActionUIState
    object Loading : PlanetActionUIState
}

data class UIStatePlanet(
    val planets: PlanetsListUIState = PlanetsListUIState.Loading,
    var planet: PlanetDetailUIState = PlanetDetailUIState.Loading,
    var planetAction: PlanetActionUIState = PlanetActionUIState.Loading
)

@HiltViewModel
@Keep
class PlanetViewModel @Inject constructor(
    private val repository: IPlanetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIStatePlanet())
    val uiState = _uiState.asStateFlow()

    fun getAllPlanets(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(planets = PlanetsListUIState.Loading) }
            _uiState.update {
                val state = runCatching { repository.getAllPlanets(search) }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") PlanetsListUIState.Success(res.data!!.planets)
                        else PlanetsListUIState.Error(res.message)
                    },
                    onFailure = { e -> PlanetsListUIState.Error(e.message ?: "Unknown error") }
                )
                it.copy(planets = state)
            }
        }
    }

    fun getPlanetById(planetId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(planet = PlanetDetailUIState.Loading) }
            _uiState.update {
                val state = runCatching { repository.getPlanetById(planetId) }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") PlanetDetailUIState.Success(res.data!!.planet)
                        else PlanetDetailUIState.Error(res.message)
                    },
                    onFailure = { e -> PlanetDetailUIState.Error(e.message ?: "Unknown error") }
                )
                it.copy(planet = state)
            }
        }
    }

    fun postPlanet(
        namaPlanet: String, deskripsi: String, jarakDariMatahari: String,
        diameter: String, jumlahSatelit: Int, tipePlanet: String, file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(planetAction = PlanetActionUIState.Loading) }
            _uiState.update {
                val state = runCatching {
                    repository.postPlanet(namaPlanet, deskripsi, jarakDariMatahari, diameter, jumlahSatelit, tipePlanet, file)
                }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") PlanetActionUIState.Success(res.data!!.planetId)
                        else PlanetActionUIState.Error(res.message)
                    },
                    onFailure = { e -> PlanetActionUIState.Error(e.message ?: "Unknown error") }
                )
                it.copy(planetAction = state)
            }
        }
    }

    fun putPlanet(
        planetId: String, namaPlanet: String, deskripsi: String,
        jarakDariMatahari: String, diameter: String, jumlahSatelit: Int, tipePlanet: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(planetAction = PlanetActionUIState.Loading) }
            _uiState.update {
                val state = runCatching {
                    repository.putPlanet(planetId, namaPlanet, deskripsi, jarakDariMatahari, diameter, jumlahSatelit, tipePlanet)
                }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") PlanetActionUIState.Success(res.message)
                        else PlanetActionUIState.Error(res.message)
                    },
                    onFailure = { e -> PlanetActionUIState.Error(e.message ?: "Unknown error") }
                )
                it.copy(planetAction = state)
            }
        }
    }

    fun deletePlanet(planetId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(planetAction = PlanetActionUIState.Loading) }
            _uiState.update {
                val state = runCatching { repository.deletePlanet(planetId) }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") PlanetActionUIState.Success(res.message)
                        else PlanetActionUIState.Error(res.message)
                    },
                    onFailure = { e -> PlanetActionUIState.Error(e.message ?: "Unknown error") }
                )
                it.copy(planetAction = state)
            }
        }
    }
}