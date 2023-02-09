package ua.bvar.rickmortyapp.ui.screen.home.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ua.bvar.rickmortyapp.NavDestinations
import ua.bvar.rickmortyapp.R
import ua.bvar.rickmortyapp.ui.screen.common.list_item.CharacterListItem
import ua.bvar.rickmortyapp.ui.screen.home.search.contract.SearchViewState

@Composable
fun SearchScreen(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    searchViewModel: SearchViewModel
) {
    var queryInput by remember { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()
    val state by searchViewModel.state.subscribeAsState(SearchViewState())
    val event by searchViewModel.event.subscribeAsState(null)

    val listItems = state?.list ?: emptyList()
    val internetConnection = state?.internetConnection == true

    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItem?.let { it.index >= (totalItemsCount - 3) } ?: false
        }
    }
    if (isLastItemVisible) {
        searchViewModel.loadNextPage()
    }

    event?.let {
        val message = stringResource(id = it.stringRes)
        LaunchedEffect(key1 = message) {
            coroutine.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(visible = !internetConnection) {
                Text(
                    text = stringResource(R.string.search_no_internet_connection),
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.error)
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.caption
                )
            }
            OutlinedTextField(
                value = queryInput,
                enabled = internetConnection,
                onValueChange = { queryInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textStyle = MaterialTheme.typography.body1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { searchViewModel.search(queryInput) }),
                singleLine = true,
                placeholder = { Text(stringResource(R.string.search_input_label)) }
            )
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(listItems.size) { index ->
                    CharacterListItem(
                        item = listItems[index],
                        onItemClick = {
                            navController.navigate("${NavDestinations.CHARACTER_DETAILS}/${it.id}")
                        },
                        onFavoriteClick = { searchViewModel.toggleFavorite(it.id) }
                    )
                }
            }

        }
    }
}

