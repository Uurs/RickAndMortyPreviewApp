package ua.bvar.rickmortyapp.ui.screen.home.favorites.contract

import ua.bvar.rickmortyapp.R

sealed class FavoriteViewEvent(val stringRes: Int) {
    object FailedFetchData : FavoriteViewEvent(R.string.favorites_failed_to_fetch_data)
    object FailedToggleFavorite : FavoriteViewEvent(R.string.favorites_failed_to_toggle_favorite)
}