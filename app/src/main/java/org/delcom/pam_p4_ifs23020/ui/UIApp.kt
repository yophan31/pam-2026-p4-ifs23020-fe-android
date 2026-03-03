package org.delcom.pam_p4_ifs23020.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23020.helper.ConstHelper
import org.delcom.pam_p4_ifs23020.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23020.ui.screens.*
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlantViewModel
import org.delcom.pam_p4_ifs23020.ui.viewmodels.PlanetViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController: NavHostController = rememberNavController(),
    plantViewModel: PlantViewModel,
    planetViewModel: PlanetViewModel   // ← TAMBAH
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                CustomSnackbar(data, onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() })
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier = Modifier.fillMaxSize().background(Color(0xFFF7F8FA))
        ) {
            composable(ConstHelper.RouteNames.Home.path) {
                HomeScreen(navController = navController)
            }
            composable(ConstHelper.RouteNames.Profile.path) {
                ProfileScreen(navController = navController, plantViewModel = plantViewModel)
            }
            // Plants routes
            composable(ConstHelper.RouteNames.Plants.path) {
                PlantsScreen(navController = navController, plantViewModel = plantViewModel)
            }
            composable(ConstHelper.RouteNames.PlantsAdd.path) {
                PlantsAddScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel)
            }
            composable(
                route = ConstHelper.RouteNames.PlantsDetail.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStack ->
                PlantsDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId = backStack.arguments?.getString("plantId") ?: ""
                )
            }
            composable(
                route = ConstHelper.RouteNames.PlantsEdit.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStack ->
                PlantsEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId = backStack.arguments?.getString("plantId") ?: ""
                )
            }

            // ⭐ Planets routes
            composable(ConstHelper.RouteNames.Planets.path) {
                PlanetsScreen(navController = navController, planetViewModel = planetViewModel)
            }
            composable(ConstHelper.RouteNames.PlanetsAdd.path) {
                PlanetsAddScreen(navController = navController, snackbarHost = snackbarHostState, planetViewModel = planetViewModel)
            }
            composable(
                route = ConstHelper.RouteNames.PlanetsDetail.path,
                arguments = listOf(navArgument("planetId") { type = NavType.StringType })
            ) { backStack ->
                PlanetsDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    planetViewModel = planetViewModel,
                    planetId = backStack.arguments?.getString("planetId") ?: ""
                )
            }
            composable(
                route = ConstHelper.RouteNames.PlanetsEdit.path,
                arguments = listOf(navArgument("planetId") { type = NavType.StringType })
            ) { backStack ->
                PlanetsEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    planetViewModel = planetViewModel,
                    planetId = backStack.arguments?.getString("planetId") ?: ""
                )
            }
        }
    }
}