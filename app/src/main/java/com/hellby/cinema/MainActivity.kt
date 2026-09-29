package com.hellby.cinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hellby.cinema.domain.model.ThemeMode
import com.hellby.cinema.ui.home.HomeScreen
import com.hellby.cinema.ui.home.HomeViewModel
import com.hellby.cinema.ui.settings.SettingsViewModel
import com.hellby.cinema.ui.theme.CinemaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class DetailRoute(val showId: Int)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val themeMode by settingsViewModel.themeMode.collectAsStateWithLifecycle()
            val useDarkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }
            window.statusBarColor = androidx.compose.material3.MaterialTheme.colorScheme.background.toArgb()
            window.navigationBarColor = androidx.compose.material3.MaterialTheme.colorScheme.background.toArgb()

            CinemaTheme(darkTheme = useDarkTheme) {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = HomeRoute) {
                    composable<HomeRoute> {
                        val homeViewModel: HomeViewModel = hiltViewModel()
                        HomeScreen(viewModel = homeViewModel, navController = navController)
                    }
                    composable<DetailRoute> { backStackEntry ->
                        val showId = backStackEntry.arguments?.getInt("showId") ?: 0
                        com.hellby.cinema.ui.detail.DetailScreen(
                            showId = showId,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}