package org.delcom.pam_p4_ifs23020.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23020.helper.ConstHelper
import org.delcom.pam_p4_ifs23020.helper.RouteHelper
import org.delcom.pam_p4_ifs23020.helper.SuspendHelper
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanetData
import org.delcom.pam_p4_ifs23020.ui.components.*
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetActionUIState
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetDetailUIState
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetViewModel

@Composable
fun PlanetsDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    planetViewModel: PlanetViewModel,
    planetId: String
) {
    val uiState by planetViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }
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
            }
        }
    }

    LaunchedEffect(uiState.planetAction) {
        when (val state = uiState.planetAction) {
            is PlanetActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SuspendHelper.SnackBarType.SUCCESS, state.message)
                RouteHelper.to(navController, ConstHelper.RouteNames.Planets.path, true)
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

    val menuItems = listOf(
        TopAppBarMenuItem("Ubah Data", Icons.Filled.Edit, onClick = {
            RouteHelper.to(navController,
                ConstHelper.RouteNames.PlanetsEdit.path.replace("{planetId}", planet!!.id))
        }),
        TopAppBarMenuItem("Hapus Data", Icons.Filled.Delete, onClick = {
            isConfirmDelete = true
        }, isDestructive = true)
    )

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        TopAppBarComponent(
            navController = navController,
            title = planet!!.namaPlanet,
            showBackButton = true,
            customMenuItems = menuItems
        )
        Box(modifier = Modifier.weight(1f)) {
            PlanetsDetailUI(planet = planet!!)
            BottomDialog(
                type = BottomDialogType.ERROR,
                show = isConfirmDelete,
                onDismiss = { isConfirmDelete = false },
                title = "Hapus Planet",
                message = "Yakin ingin menghapus data ${planet!!.namaPlanet}?",
                confirmText = "Ya, Hapus",
                onConfirm = {
                    isLoading = true
                    planetViewModel.deletePlanet(planetId)
                },
                cancelText = "Batal",
                destructiveAction = true
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun PlanetsDetailUI(planet: ResponsePlanetData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(getPlanetEmoji(planet.tipePlanet), style = MaterialTheme.typography.displayLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    planet.namaPlanet,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                ) {
                    Text(
                        planet.tipePlanet,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Stats row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PlanetStatCard(emoji = "🛸", label = "Satelit", value = "${planet.jumlahSatelit}", modifier = Modifier.weight(1f))
            PlanetStatCard(emoji = "📏", label = "Diameter", value = planet.diameter, modifier = Modifier.weight(1f))
        }
        PlanetStatCard(emoji = "☀️", label = "Jarak dari Matahari", value = planet.jarakDariMatahari)

        // Deskripsi
        PlanetInfoCard(title = "📖 Deskripsi", content = planet.deskripsi)
    }
}

@Composable
fun PlanetStatCard(emoji: String, label: String, value: String, modifier: Modifier = Modifier.fillMaxWidth()) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun PlanetInfoCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}