package io.github.mohamedisoliman.mvi.login

/**
 *
 * Created by Mohamed Ibrahim on 9/29/18.
 */
class LoginResult {
    private val token = "ndkslgnsdmdfvfdlb"
}

sealed class LoginError : Throwable() {
    data class AuthorizationError(val errorMessage: String) : LoginError()
    data class PasswordNotCorrect(val errorMessage: String) : LoginError()
    data class TooManyAttempts(val errorMessage: String) : LoginError()
    class UnknwonError : LoginError()
}