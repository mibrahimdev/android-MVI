package io.github.mohamedisoliman.mvi.usecase

import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult.Failure
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult.InFlight
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult.Success
import io.github.mohamedisoliman.mvi.ui.ReposAction
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
class GetGithubRepos(val repository: Repository) {

  fun execute(action: ReposAction): Observable<LoadingReposResult> {
    return Observable.just(action)
        .subscribeOn(Schedulers.io())
        .flatMap { incomingActions ->
          when (incomingActions) {
            is ReposAction.InitialAction -> getRepos()
            is ReposAction.RefreshRepos -> getRepos()//just refresh the list
            is ReposAction.GetMoreItems -> getRepos(incomingActions.lastId)
            is ReposAction.BookmarkRepo -> Observable.just(LoadingReposResult.DUMMY)//TODO
          }
        }
  }

  private fun getRepos(since: Long = 1): Observable<LoadingReposResult>? {
    return repository.getGithubRepositories(since)
        .map<LoadingReposResult> { Success(it) }
        .cast(LoadingReposResult::class.java)
        .startWith(InFlight)
        .onErrorReturn { Failure(it) }
  }
}