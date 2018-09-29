package io.github.mohamedisoliman.mvi.mymvi

import io.github.mohamedisoliman.mvi.mymvi.base.MviViewModel
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

/**
 *
 * Created by Mohamed Ibrahim on 9/27/18.
 */

class LoginViewModel(val mviView: MviLoginView) : MviViewModel(mviView) {

    fun setupLogin() {
        mviView.loginClick()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { mviView.render(LoginViewState.Loading(true)) }
                .flatMap { loginApi(it.username, it.password) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleLoginResult())

    }

    private fun handleLoginResult(): Observer<Boolean> {
        return object : Observer<Boolean> {
            override fun onComplete() {
                mviView.render(LoginViewState.Loading(true))
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Boolean) {
                if (t) mviView.render(LoginViewState.Success("Hello There"))
                else mviView.render(LoginViewState.Error("Something went wrong!"))
            }

            override fun onError(e: Throwable) {
                mviView.render(LoginViewState.Error("Something went wrong!"))
            }

        }
    }


    //interactor methods
    private fun loginApi(username: String, password: String): Observable<Boolean> {
        return Observable.zip(Observable.just(username), Observable.just(password),
                BiFunction<String, String, Boolean>
                { username, password -> username == "Mohamed" && password == "12345678" })
                .delay(10, TimeUnit.SECONDS)
    }
}