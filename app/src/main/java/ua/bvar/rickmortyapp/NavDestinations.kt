package ua.bvar.rickmortyapp

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.navArgument

object NavDestinations {
    const val HOME = "home"
    const val CHARACTER_DETAILS = "character_details"

    const val SEARCH = "search"
    const val FAVORITES = "favorites"
}

object NavArgs {
    const val CHARACTER_ID = "character_id"
}