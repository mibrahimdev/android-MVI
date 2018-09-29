package io.github.mohamedisoliman.mvi

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.github.mohamedisoliman.mvi.login.LoginViewModel
import io.github.mohamedisoliman.mvi.login.MviLoginView

/**
 * Created by Mohamed Ibrahim on 9/27/18.
 */
class ViewModelFactory(private val mParam: MviLoginView) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(mParam) as T
    }
}
