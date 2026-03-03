package org.delcom.pam_p4_ifs23020.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23020.helper.ConstHelper
import org.delcom.pam_p4_ifs23020.helper.RouteHelper
import org.delcom.pam_p4_ifs23020.helper.SuspendHelper
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanetData
import org.delcom.pam_p4_ifs23020.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23020.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23020.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetActionUIState
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetDetailUIState
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetViewModel

@Composable
fun PlanetsEditScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    planetViewModel: PlanetViewModel,
    planetId: String
) {
    val uiState by planetViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var planet by remember { mutableStateOf<ResponsePlanetData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        uiState.planetAction = PlanetActionUIState.Loading
        uiState.planet = PlanetDetailUIState.Loading
        planetViewModel.getPlanetById(planetId)
    }

    LaunchedEffect(uiState.planet) {
        if (uiState.planet !is PlanetDetailUIState.Loading) {
            if (uiState.planet is PlanetDetailUIState.Success) {
                planet = (uiState.planet as PlanetDetailUIState.Success).data
                isLoading = false
            } else {
                RouteHelper.back(navController)
                isLoading = false
            }
        }
    }

    LaunchedEffect(uiState.planetAction) {
        when (val state = uiState.planetAction) {
            is PlanetActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.SUCCESS, state.message)
                RouteHelper.to(
                    navController = navController,
                    destination = ConstHelper.RouteNames.PlanetsDetail.path.replace("{planetId}", planetId),
                    popUpTo = ConstHelper.RouteNames.PlanetsDetail.path.replace("{planetId}", planetId),
                    removeBackStack = true
                )
                isLoading = false
            }
            is PlanetActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.ERROR, state.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading || planet == null) { LoadingUI(); return }

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        TopAppBarComponent(navController = navController, title = "Ubah Data Planet", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            PlanetsEditUI(planet = planet!!, onSave = { nama, desk, jarak, diam, sat, tipe ->
                isLoading = true
                planetViewModel.putPlanet(planetId, nama, desk, jarak, diam, sat, tipe)
            })
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun PlanetsEditUI(
    planet: ResponsePlanetData,
    onSave: (String, String, String, String, Int, String) -> Unit
) {
    var namaPlanet by remember { mutableStateOf(planet.namaPlanet) }
    var deskripsi by remember { mutableStateOf(planet.deskripsi) }
    var jarakDariMatahari by remember { mutableStateOf(planet.jarakDariMatahari) }
    var diameter by remember { mutableStateOf(planet.diameter) }
    var jumlahSatelit by remember { mutableStateOf(planet.jumlahSatelit.toString()) }
    var tipePlanet by remember { mutableStateOf(planet.tipePlanet) }
    var showError by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(getPlanetEmoji(tipePlanet), style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.width(12.dp))
                    Text("Edit ${planet.namaPlanet}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            if (showError.isNotEmpty()) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(showError, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(12.dp))
                }
            }

            PlanetTextField(value = namaPlanet, onValueChange = { namaPlanet = it }, label = "Nama Planet", imeAction = ImeAction.Next)
            PlanetTextField(value = deskripsi, onValueChange = { deskripsi = it }, label = "Deskripsi",
                imeAction = ImeAction.Next, minLines = 3, maxLines = 5, modifier = Modifier.height(120.dp))
            PlanetTextField(value = jarakDariMatahari, onValueChange = { jarakDariMatahari = it }, label = "Jarak dari Matahari", imeAction = ImeAction.Next)
            PlanetTextField(value = diameter, onValueChange = { diameter = it }, label = "Diameter", imeAction = ImeAction.Next)
            PlanetTextField(value = jumlahSatelit, onValueChange = { jumlahSatelit = it }, label = "Jumlah Satelit",
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Number)
            PlanetTextField(value = tipePlanet, onValueChange = { tipePlanet = it }, label = "Tipe Planet",
                imeAction = ImeAction.Done, onDone = { focusManager.clearFocus() })

            Spacer(modifier = Modifier.height(64.dp))
        }

        FloatingActionButton(
            onClick = {
                when {
                    namaPlanet.isBlank() -> showError = "Nama planet tidak boleh kosong!"
                    deskripsi.isBlank() -> showError = "Deskripsi tidak boleh kosong!"
                    jarakDariMatahari.isBlank() -> showError = "Jarak dari matahari tidak boleh kosong!"
                    diameter.isBlank() -> showError = "Diameter tidak boleh kosong!"
                    jumlahSatelit.isBlank() -> showError = "Jumlah satelit tidak boleh kosong!"
                    tipePlanet.isBlank() -> showError = "Tipe planet tidak boleh kosong!"
                    else -> {
                        showError = ""
                        onSave(namaPlanet, deskripsi, jarakDariMatahari, diameter,
                            jumlahSatelit.toIntOrNull() ?: 0, tipePlanet)
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Save, contentDescription = "Simpan")
        }
    }
}