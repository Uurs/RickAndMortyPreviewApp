package ua.bvar.data.core

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ua.bvar.data.Constants.DEFAULT_NETWORK_CHECK_DELAY
import ua.bvar.data.Constants.DEFAULT_NETWORK_CHECK_TIMEOUT
import ua.bvar.data.Constants.DEFAULT_NETWORK_CHECK_URL
import ua.bvar.data.repository.InternetConnectionObserver
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class AndroidInternetConnectionObserver @Inject constructor() :
    InternetConnectionObserver {

    override fun observeNetworkState(): Observable<Boolean> {
        return Observable
            .interval(
                DEFAULT_NETWORK_CHECK_DELAY,
                TimeUnit.MILLISECONDS,
                Schedulers.io()
            )
            .map { getNetworkState() }
            .onErrorComplete()
    }

    override fun getNetworkState(): Boolean {
        return try {
            InetAddress
                .getByName(DEFAULT_NETWORK_CHECK_URL)
                .isReachable(DEFAULT_NETWORK_CHECK_TIMEOUT)
        } catch (e: Exception) {
            false
        }
    }
}