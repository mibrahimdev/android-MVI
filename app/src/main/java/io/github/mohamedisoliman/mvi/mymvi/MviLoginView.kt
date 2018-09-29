package io.github.mohamedisoliman.mvi.mymvi

import io.github.mohamedisoliman.mvi.mymvi.base.MviView
import io.github.mohamedisoliman.mvi.mymvi.base.MviViewState
import io.reactivex.Observable

/**
 *
 * Created by Mohamed Ibrahim on 9/27/18.
 */
interface MviLoginView : MviView {

    fun loginClick(): Observable<LoginAction>

}