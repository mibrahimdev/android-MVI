package io.github.mohamedisoliman.mvi.mymvi

import io.github.mohamedisoliman.mvi.mymvi.base.MviViewState

/**
 *
 * Created by Mohamed Ibrahim on 9/27/18.
 */
sealed class LoginViewState : MviViewState {

    data class Loading(val isLoading: Boolean) : LoginViewState()

    data class Error(val message: String) : LoginViewState()

    data class Success(val message: String) : LoginViewState()

    data class InvalidUsername(val message: String) : LoginViewState()

    data class InvalidPassword(val message: String) : LoginViewState()

}