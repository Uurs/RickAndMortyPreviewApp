package ua.bvar.rickmortyapp.ui.screen.common.list_item

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ua.bvar.rickmortyapp.R
import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem
import ua.bvar.rickmortyapp.ui.theme.iconTintFavorite
import ua.bvar.rickmortyapp.ui.theme.iconTintNotFavorite

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CharacterListItem(
    item: CharacterItem,
    onItemClick: (CharacterItem) -> Unit,
    onFavoriteClick: (CharacterItem) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onItemClick(item) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (item.icon != null) {
                GlideImage(
                    modifier = Modifier.size(96.dp),
                    model = item.icon,
                    contentDescription = stringResource(
                        R.string.search_list_item_image_content_description
                    )
                ) { it.thumbnail().override(96.dp.value.toInt()) }
            } else {
                Image(
                    modifier = Modifier.size(96.dp),
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = stringResource(
                        R.string.search_list_item_image_content_description
                    )
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f)
            ) {
                Text(item.name, style = MaterialTheme.typography.h5)
                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Column {
                        Text(
                            stringResource(R.string.search_list_item_species_hint),
                            style = MaterialTheme.typography.caption
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        Text(
                            item.species ?: stringResource(R.string.search_list_item_species_placeholder),
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    Column {
                        Text(
                            stringResource(R.string.search_list_item_status_hint),
                            style = MaterialTheme.typography.caption
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        Text(
                            item.status ?: stringResource(R.string.search_list_item_status_placeholder),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
            IconButton(
                onClick = { onFavoriteClick(item) },
                modifier = Modifier.wrapContentSize(),
            ) {
                if (item.isFavorite) {
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
    }
}