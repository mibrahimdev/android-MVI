package io.github.mohamedisoliman.mvi.githubrepos.presentation

import android.arch.lifecycle.ViewModel
import io.github.mohamedisoliman.mvi.MVIApp
import io.github.mohamedisoliman.mvi.githubrepos.data.Repository
import io.github.mohamedisoliman.mvi.githubrepos.data.RepositoryItem
import io.github.mohamedisoliman.mvi.githubrepos.data.RepositoryOwner
import io.github.mohamedisoliman.mvi.githubrepos.data.entities.Owner
import io.github.mohamedisoliman.mvi.githubrepos.mvibase.MviViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by Mohamed Ibrahim on 10/20/18.
 */
class ReposViewModel : ViewModel(), MviViewModel<ReposIntent, ReposViewState> {

    val reposRepistory: Repository by lazy { MVIApp.appDependencies.reposRepository }


    //TEST API the regular way
    fun loadRepos() =
//    Observable.just(listOf(RepositoryItem("AndroidDev", "2222", RepositoryOwner("Mohamed", ""))))
            reposRepistory.getGithubRepositories()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    override fun processIntents(intents: Observable<ReposIntent>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun states(): Observable<ReposViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}