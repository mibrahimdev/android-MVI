package io.github.mohamedisoliman.mvi.base

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

/**
 *
 * Created by Mohamed Ibrahim on 9/29/18.
 */
open class MviViewModel(val mvi: MviView) : ViewModel() {

    private val disposables = CompositeDisposable()


    fun subscribeViewState(observable: Observable<out MviViewState>) {
        disposables.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(CommonMviObserver(mvi)))
    }


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

}

class CommonMviObserver(val mvi: MviView) : DisposableObserver<MviViewState>() {
    override fun onNext(t: MviViewState) {
        mvi.render(t)
    }

    override fun onError(e: Throwable) {
        throw IllegalStateException("Mvi shouldn't consume errors, please convert ${e.cause} to ViewState")
    }

    override fun onComplete() {
        //DO nothing
    }


}

interface MviViewState

interface MviView {

    fun render(viewState: MviViewState)
}