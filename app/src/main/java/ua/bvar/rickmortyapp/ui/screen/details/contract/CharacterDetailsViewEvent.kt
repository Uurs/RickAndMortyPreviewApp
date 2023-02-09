package ua.bvar.rickmortyapp.ui.screen.details.contract

import ua.bvar.rickmortyapp.R

sealed class CharacterDetailsViewEvent(val stringRes: Int) {
    object FailedToGetData: CharacterDetailsViewEvent(R.string.character_details_failed_to_load_data)

}
