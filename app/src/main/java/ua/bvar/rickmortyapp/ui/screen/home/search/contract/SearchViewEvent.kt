package ua.bvar.rickmortyapp.ui.screen.home.search.contract

import ua.bvar.rickmortyapp.R

sealed class SearchViewEvent(val stringRes: Int) {
    object FailedToToGetData : SearchViewEvent(R.string.search_failed_to_load_characters)
}
