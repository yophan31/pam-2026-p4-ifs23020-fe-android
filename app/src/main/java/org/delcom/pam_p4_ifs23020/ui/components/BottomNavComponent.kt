package org.delcom.pam_p4_ifs23020.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23020.helper.ConstHelper
import org.delcom.pam_p4_ifs23020.helper.RouteHelper
import org.delcom.pam_p4_ifs23020.ui.theme.DelcomTheme

sealed class MenuBottomNav(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val iconActive: ImageVector,
) {
    object Home : MenuBottomNav(ConstHelper.RouteNames.Home.path, "Home", Icons.Outlined.Home, Icons.Filled.Home)
    object Plants : MenuBottomNav(ConstHelper.RouteNames.Plants.path, "Plants", Icons.Outlined.Nature, Icons.Filled.Nature)
    object Planets : MenuBottomNav(ConstHelper.RouteNames.Planets.path, "Planets", Icons.Outlined.Public, Icons.Filled.Public)
    object Profile : MenuBottomNav(ConstHelper.RouteNames.Profile.path, "Profile", Icons.Outlined.Person, Icons.Filled.Person)
}

@Composable
fun BottomNavComponent(navController: NavHostController) {
    val items = listOf(
        MenuBottomNav.Home,
        MenuBottomNav.Plants,
        MenuBottomNav.Planets,
        MenuBottomNav.Profile,
    )

    val currentRoute = navController.currentDestination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.height(120.dp),
            tonalElevation = 0.dp
        ) {
            items.forEach { screen ->
                val selected = currentRoute?.contains(screen.route) == true
                val animatedHeight by animateDpAsState(
                    targetValue = if (selected) 56.dp else 48.dp,
                    label = "navigationHeight"
                )

                NavigationBarItem(
                    selected = selected,
                    onClick = { RouteHelper.to(navController, screen.route, true) },
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            NavigationIcon(selected = selected, screen = screen)
                        }
                    },
                    modifier = Modifier
                        .height(animatedHeight)
                        .padding(vertical = if (selected) 0.dp else 4.dp),
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationIcon(
    selected: Boolean,
    screen: MenuBottomNav,
    hasNotification: Boolean = false
) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape)
            )
        }
        BadgedBox(badge = {
            if (hasNotification && !selected) {
                Badge(
                    modifier = Modifier.size(8.dp).offset(x = 6.dp, y = (-6).dp),
                    containerColor = MaterialTheme.colorScheme.error
                )
            }
        }) {
            Icon(
                imageVector = if (selected) screen.iconActive else screen.icon,
                contentDescription = screen.title,
                modifier = Modifier.size(24.dp),
                tint = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}