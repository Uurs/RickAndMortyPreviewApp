package ua.bvar.domain.usecase_impl

import io.reactivex.rxjava3.core.Observable
import ua.bvar.data.repository.InternetConnectionObserver
import ua.bvar.domain.model.InternetConnection
import ua.bvar.domain.usecase.GetInternetConnectionUseCase
import javax.inject.Inject

internal class GetInternetConnectionUseCaseImpl @Inject constructor(
    private val internetConnectionObserver: InternetConnectionObserver
) : GetInternetConnectionUseCase {

    override fun execute(): Observable<InternetConnection> {
        return internetConnectionObserver.observeNetworkState()
            .map { InternetConnection(it) }
    }
}