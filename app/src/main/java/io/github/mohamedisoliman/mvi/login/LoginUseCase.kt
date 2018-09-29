package io.github.mohamedisoliman.mvi.login

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *
 * Created by Mohamed Ibrahim on 9/29/18.
 */
class LoginUseCase(private val loginAction: LoginAction) {
    private val correctPassword = "12345678"
    private val correctUsername = "Mohamed"

    fun execute(): Observable<LoginResult> {
        return Observable.just(loginAction)
                .delay(10, TimeUnit.SECONDS)
                .flatMap { validate(it) }
                .subscribeOn(Schedulers.io())
    }


    //mocking back-end validation
    private fun validate(loginAction: LoginAction): Observable<LoginResult> =
            if (loginAction.username == correctUsername && loginAction.password == correctPassword)
                Observable.just(LoginResult())
            else if (loginAction.password != correctPassword && loginAction.username != correctUsername)
                throw LoginError.AuthorizationError("Sorry Not Authorized")
            else if (loginAction.password != correctPassword)
                throw LoginError.PasswordNotCorrect("Do you Forget your password, we can send you reset mail")
            else throw LoginError.UnknwonError()
}