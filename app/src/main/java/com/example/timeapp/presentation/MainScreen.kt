package com.example.timeapp.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.timeapp.presentation.alarm.AlarmScreen
import com.example.timeapp.presentation.stopwatch.StopwatchScreen
import com.example.timeapp.presentation.timer.TimerScreen
import com.example.timeapp.presentation.world_clock.AddCityScreen
import com.example.timeapp.presentation.world_clock.WorldClockScreen
import kotlinx.serialization.Serializable

@Composable
fun MainScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.LightGray
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                topLevelRoutes.forEach { topLevelRoute ->

                    BottomNavigationItem(
                        modifier = Modifier.padding(8.dp),
                        icon = { Icon(topLevelRoute.icon, contentDescription = null) },
                        label = {
                            Text(
                                text = topLevelRoute.name,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true,
                        onClick = {
                            navController.navigate(topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WorldClockScreen,
            Modifier.padding(innerPadding)
        ) {
            composable<WorldClockScreen> {
                WorldClockScreen(nv = navController)
            }
            composable<AddCityScreen> {
                AddCityScreen(nv = navController)
            }
            composable<AlarmScreen> {
                AlarmScreen()
            }
            composable<StopwatchScreen> {
                StopwatchScreen()
            }
            composable<TimerScreen> {
                TimerScreen()
            }
        }
    }
}

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("World Clock", WorldClockScreen, Icons.Default.Star),
    TopLevelRoute("Alarm", AlarmScreen, Icons.Default.Star),
    TopLevelRoute("Stopwatch", StopwatchScreen, Icons.Default.Star),
    TopLevelRoute("Timer", TimerScreen, Icons.Default.Star)
)

@Serializable
object WorldClockScreen

@Serializable
object AddCityScreen

@Serializable
object AlarmScreen

@Serializable
object StopwatchScreen

@Serializable
object TimerScreen