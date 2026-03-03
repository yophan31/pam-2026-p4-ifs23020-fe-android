package org.delcom.pam_p4_ifs23020.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23020.helper.ConstHelper
import org.delcom.pam_p4_ifs23020.helper.RouteHelper
import org.delcom.pam_p4_ifs23020.network.planets.data.ResponsePlanetData
import org.delcom.pam_p4_ifs23020.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23020.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23020.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetViewModel
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetsListUIState

@Composable
fun PlanetsScreen(
    navController: NavHostController,
    planetViewModel: PlanetViewModel
) {
    val uiState by planetViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var planets by remember { mutableStateOf<List<ResponsePlanetData>>(emptyList()) }

    fun fetchData() {
        isLoading = true
        planetViewModel.getAllPlanets(searchQuery.text)
    }

    LaunchedEffect(Unit) { fetchData() }

    LaunchedEffect(uiState.planets) {
        if (uiState.planets !is PlanetsListUIState.Loading) {
            isLoading = false
            planets = if (uiState.planets is PlanetsListUIState.Success)
                (uiState.planets as PlanetsListUIState.Success).data
            else emptyList()
        }
    }

    if (isLoading) { LoadingUI(); return }

    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = "Daftar Planet",
            showBackButton = false,
            withSearch = true,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearchAction = { fetchData() }
        )
        Box(modifier = Modifier.weight(1f)) {
            PlanetsListUI(planets = planets, onOpen = { id ->
                RouteHelper.to(navController, "planets/$id")
            })
            FloatingActionButton(
                onClick = { RouteHelper.to(navController, ConstHelper.RouteNames.PlanetsAdd.path) },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Planet")
            }
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun PlanetsListUI(planets: List<ResponsePlanetData>, onOpen: (String) -> Unit) {
    if (planets.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = "🪐 Tidak ada data planet!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(24.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(planets) { planet ->
                PlanetItemUI(planet = planet, onOpen = onOpen)
            }
        }
    }
}

@Composable
fun PlanetItemUI(planet: ResponsePlanetData, onOpen: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onOpen(planet.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Planet icon/emoji based on type
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = getPlanetEmoji(planet.tipePlanet),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = planet.namaPlanet,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = planet.tipePlanet,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = planet.deskripsi,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoChip(label = "🛸 ${planet.jumlahSatelit} Satelit")
                    InfoChip(label = "📏 ${planet.diameter}")
                }
            }
        }
    }
}

@Composable
fun InfoChip(label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

fun getPlanetEmoji(tipePlanet: String): String {
    return when (tipePlanet.lowercase()) {
        "gas giant", "raksasa gas" -> "🪐"
        "ice giant", "raksasa es" -> "🔵"
        "terrestrial", "terestrial", "rocky", "berbatu" -> "🌍"
        "dwarf", "katai" -> "⚫"
        else -> "🌑"
    }
}