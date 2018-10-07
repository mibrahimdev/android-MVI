package io.github.mohamedisoliman.mvi.login

import io.github.mohamedisoliman.mvi.base.MviViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 *
 * Created by Mohamed Ibrahim on 9/27/18.
 */

class LoginViewModel(private val mviView: MviLoginView) : MviViewModel(mviView) {

    fun setupLogin() {
        val observable: Observable<LoginViewState> = mviView.loginClick()
                .switchMap { loginAction ->
                    LoginUseCase(loginAction).execute()
                            .map<LoginViewState> { LoginViewState.Success("Hello there") }
                            .onErrorReturn { error ->
                                when (error) {
                                    is LoginError.AuthorizationError -> LoginViewState.Error("Sorry Not Authorized")
                                    is LoginError.PasswordNotCorrect -> LoginViewState.Error(error.errorMessage)
                                    else -> LoginViewState.Error("Something Went wrong")
                                }
                            }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoginViewState.Loading(true))

                }

        subscribeViewState(observable)
    }

}