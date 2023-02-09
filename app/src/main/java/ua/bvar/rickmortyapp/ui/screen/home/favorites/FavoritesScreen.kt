package ua.bvar.rickmortyapp.ui.screen.home.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ua.bvar.rickmortyapp.NavDestinations
import ua.bvar.rickmortyapp.R
import ua.bvar.rickmortyapp.ui.screen.common.list_item.CharacterListItem
import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem
import ua.bvar.rickmortyapp.ui.screen.home.favorites.contract.FavoritesViewState

@Composable
fun FavoritesScreen(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    viewModel: FavoritesViewModel
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.subscribeAsState(FavoritesViewState())
    val event by viewModel.event.subscribeAsState(null)

    event?.let {
        val message = stringResource(id = it.stringRes)
        LaunchedEffect(key1 = message) {
            scope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Surface {
        if (state.list.isEmpty()) {
            EmptyFavoritesScreen()
        } else {
            ListFavoritesScreen(
                list = state.list,
                onItemClick = {
                    navController.navigate("${NavDestinations.CHARACTER_DETAILS}/${it.id}")
                },
                onFavoriteClick = { viewModel.toggleFavorite(it.id) }
            )
        }
    }
}

@Composable
private fun EmptyFavoritesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            stringResource(R.string.favorites_empty_list_message),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
private fun ListFavoritesScreen(
    list: List<CharacterItem>,
    onItemClick: (CharacterItem) -> Unit,
    onFavoriteClick: (CharacterItem) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(list.size) { index ->
            CharacterListItem(
                item = list[index],
                onItemClick = onItemClick,
                onFavoriteClick = onFavoriteClick,
            )
        }
    }
}