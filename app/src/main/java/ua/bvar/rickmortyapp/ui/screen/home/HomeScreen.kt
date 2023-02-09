package ua.bvar.rickmortyapp.ui.screen.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ua.bvar.rickmortyapp.NavDestinations
import ua.bvar.rickmortyapp.R
import ua.bvar.rickmortyapp.ui.screen.home.favorites.FavoritesScreen
import ua.bvar.rickmortyapp.ui.screen.home.favorites.FavoritesViewModel
import ua.bvar.rickmortyapp.ui.screen.home.search.SearchScreen
import ua.bvar.rickmortyapp.ui.screen.home.search.SearchViewModel

@Composable
@OptIn(ExperimentalPagerApi::class)
fun HomeScreen(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    snackbarData = data
                )
            }
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = bottomNavItems[pagerState.currentPage].screenRoute,
                onBottomNavItemClick = { item ->
                    scope.launch {
                        pagerState.animateScrollToPage(
                            bottomNavItems.indexOf(item)
                        )
                    }
                })
        },
        content = { paddingValues ->
            NavigationHost(
                snackbarHostState = scaffoldState.snackbarHostState,
                navController = navController,
                pagerState = pagerState,
                paddingValues = paddingValues
            )
        }
    )
}

private val bottomNavItems = listOf(
    BottomNavItemConfig(
        R.string.home_bottom_nav_bar_characters,
        R.drawable.ic_characters_search,
        NavDestinations.SEARCH
    ),
    BottomNavItemConfig(
        R.string.home_bottom_nav_bar_favorites,
        R.drawable.ic_favorite,
        NavDestinations.FAVORITES
    ),
)

@Composable
private fun BottomNavBar(
    currentRoute: String,
    onBottomNavItemClick: (BottomNavItemConfig) -> Unit,
) {
    BottomNavigation {
        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                selected = item.screenRoute == currentRoute,
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = stringResource(item.title)
                    )
                },
                onClick = { onBottomNavItemClick(item) })
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun NavigationHost(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    pagerState: PagerState,
    paddingValues: PaddingValues,
) {
    HorizontalPager(
        modifier = Modifier.padding(paddingValues),
        count = bottomNavItems.size,
        state = pagerState
    ) {
        when (bottomNavItems[it].screenRoute) {
            NavDestinations.SEARCH -> {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(snackbarHostState, navController, viewModel)
            }
            NavDestinations.FAVORITES -> {
                val viewModel: FavoritesViewModel = hiltViewModel()
                FavoritesScreen(snackbarHostState, navController, viewModel)
            }
        }
    }

}

private data class BottomNavItemConfig(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val screenRoute: String
)