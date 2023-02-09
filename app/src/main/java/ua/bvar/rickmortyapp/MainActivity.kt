package ua.bvar.rickmortyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ua.bvar.rickmortyapp.ui.screen.details.CharacterDetailsScreen
import ua.bvar.rickmortyapp.ui.screen.details.CharacterDetailsViewModel
import ua.bvar.rickmortyapp.ui.screen.home.HomeScreen
import ua.bvar.rickmortyapp.ui.theme.RickMortyAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            RickMortyAppTheme {
                NavHost(navController, startDestination = NavDestinations.HOME) {
                    composable(NavDestinations.HOME) {
                        HomeScreen(navController)
                    }

                    composable(
                        "${NavDestinations.CHARACTER_DETAILS}/{${NavArgs.CHARACTER_ID}}",
                        arguments = listOf(navArgument(NavArgs.CHARACTER_ID) { type = NavType.IntType })
                    ) {
                        val viewModel = hiltViewModel<CharacterDetailsViewModel>()
                        CharacterDetailsScreen(navController, viewModel)
                    }
                }
            }
        }
    }
}