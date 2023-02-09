package ua.bvar.domain.usecase

import io.reactivex.rxjava3.core.Maybe
import ua.bvar.domain.model.RMCharacter

interface ToggleFavoriteUseCase {
    fun execute(id: Int): Maybe<RMCharacter>
}