package ua.bvar.data.repository

import io.reactivex.rxjava3.core.Observable

interface InternetConnectionObserver {
    fun observeNetworkState(): Observable<Boolean>
    fun getNetworkState(): Boolean
}