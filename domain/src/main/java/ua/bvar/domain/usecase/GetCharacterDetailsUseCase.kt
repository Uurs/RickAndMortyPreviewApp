package ua.bvar.domain.usecase

import io.reactivex.rxjava3.core.Single
import ua.bvar.domain.model.RMCharacterDetails

interface GetCharacterDetailsUseCase {
    fun execute(id: Int) : Single<RMCharacterDetails>
}