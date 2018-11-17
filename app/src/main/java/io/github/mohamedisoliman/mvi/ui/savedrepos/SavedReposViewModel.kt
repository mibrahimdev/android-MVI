package io.github.mohamedisoliman.mvi.ui.savedrepos

import android.arch.lifecycle.ViewModel
import io.github.mohamedisoliman.mvi.MVIApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by Mohamed Ibrahim on 11/17/18.
 */
class SavedReposViewModel : ViewModel() {

  val repository by lazy { MVIApp.appDependencies.reposRepository }

  fun getSavedRepos() =
    repository.getSavedRepos()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}