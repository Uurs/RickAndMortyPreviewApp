package ua.bvar.domain.usecase

import io.reactivex.rxjava3.core.Observable
import ua.bvar.domain.model.InternetConnection

interface GetInternetConnectionUseCase {
    fun execute(): Observable<InternetConnection>
}