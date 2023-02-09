package ua.bvar.rickmortyapp.core

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseViewModel: ViewModel() {
    private val disposable = CompositeDisposable()

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    protected inline fun <T> BehaviorSubject<T>.update(update: T.() -> T) {
        val v = value
        if (v != null) {
            val updated = v.update()
            if (updated != v) {
                onNext(updated)
            }
        }
    }

    protected fun Disposable.addToClearableDisposable() {
        disposable.add(this)
    }
}