package io.github.mohamedisoliman.mvi

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.github.mohamedisoliman.mvi.base.MviView
import io.github.mohamedisoliman.mvi.base.MviViewModel

/**
 * Created by Mohamed Ibrahim on 9/27/18.
 */
class ViewModelFactory<View : MviView>(private val mParam: View) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MviViewModel(mParam) as T
    }
}
