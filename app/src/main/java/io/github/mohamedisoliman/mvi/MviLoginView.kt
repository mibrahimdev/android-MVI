package io.github.mohamedisoliman.mvi

import io.github.mohamedisoliman.mvi.base.MviView
import io.reactivex.Observable

/**
 *
 * Created by Mohamed Ibrahim on 9/27/18.
 */
interface MviLoginView : MviView {

    fun loginClick(): Observable<LoginAction>

}