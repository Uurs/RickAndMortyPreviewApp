package ua.bvar.rickmortyapp.ui.screen.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.bvar.rickmortyapp.R
import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem
import ua.bvar.rickmortyapp.ui.screen.details.contract.CharacterDetailsViewState
import ua.bvar.rickmortyapp.ui.screen.details.contract.CharacterMetadata
import ua.bvar.rickmortyapp.ui.theme.iconTintFavorite
import ua.bvar.rickmortyapp.ui.theme.iconTintNotFavorite

@Composable
fun CharacterDetailsScreen(
    navHostController: NavHostController,
    viewModel: CharacterDetailsViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val state by viewModel.state.subscribeAsState(initial = CharacterDetailsViewState())
    val event by viewModel.event.subscribeAsState(initial = null)

    event?.let {
        val message = stringResource(id = it.stringRes)
        LaunchedEffect(key1 = message) {
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

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
        topBar = {
            TopAppBar(
                title = { Text("Character Details") },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = AppBarDefaults.TopAppBarElevation,
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            if (state.character == null || state.meta == null) {
                Text("Loading...")
            } else {
                CharacterDetailsContent(
                    character = state.character!!,
                    meta = state.meta!!,
                    toggleFavorite = { viewModel.toggleFavorite() }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CharacterDetailsContent(
    character: CharacterItem,
    meta: CharacterMetadata,
    toggleFavorite: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        GlideImage(
            model = character.icon,
            contentDescription = stringResource(id = R.string.character_details_image_content_description),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.fillMaxWidth()
            )
            IconButton(
                onClick = { toggleFavorite() },
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.TopEnd),
            ) {
                if (character.isFavorite) {
                    Image(
                        painter = painterResource(R.drawable.ic_favorite),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(iconTintFavorite)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_favorite_border),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(iconTintNotFavorite)
                    )
                }
            }

        }
        Spacer(modifier = Modifier.size(16.dp))
        Row {
            DataRaw(
                modifier = Modifier.weight(1f),
                subtitle = stringResource(id = R.string.character_details_subtitle_status),
                value = character.status
                    ?: stringResource(id = R.string.character_details_default_status)
            )
            Spacer(modifier = Modifier.size(8.dp))
            DataRaw(
                modifier = Modifier.weight(1f),
                subtitle = stringResource(id = R.string.character_details_subtitle_species),
                value = character.species
                    ?: stringResource(id = R.string.character_details_default_species)
            )
            Spacer(modifier = Modifier.size(8.dp))
            DataRaw(
                modifier = Modifier.weight(1f),
                subtitle = stringResource(id = R.string.character_details_subtitle_gender),
                value = meta.gender
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        DataRaw(
            modifier = Modifier.fillMaxWidth(),
            subtitle = stringResource(id = R.string.character_details_subtitle_origin),
            value = meta.originName
        )
        Spacer(modifier = Modifier.size(16.dp))
        DataRaw(
            modifier = Modifier.fillMaxWidth(),
            subtitle = stringResource(id = R.string.character_details_subtitle_last_known_location),
            value = meta.lastKnownLocationName
        )
    }
}

@Composable
private fun DataRaw(
    modifier: Modifier = Modifier,
    subtitle: String,
    value: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = subtitle,
            style = MaterialTheme.typography.caption,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Preview
@Composable
fun PreviewCharacterDetailsScreen() {
    CharacterDetailsContent(
        CharacterItem(
            id = 1,
            icon = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            name = "Rick Sanchez",
            species = "Human",
            status = "Alive",
            isFavorite = false,
        ),
        CharacterMetadata(
            originName = "Earth (C-137)",
            gender = "Male",
            lastKnownLocationName = "Citadel of Ricks",
        ),
        {}
    )
}