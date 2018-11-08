package io.github.mohamedisoliman.mvi.usecase

import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.presentation.LoadingReposResult
import io.github.mohamedisoliman.mvi.presentation.ReposAction
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
            is ReposAction.InitialAction ->

              repository.getGithubRepositories()
                  .map<LoadingReposResult> { LoadingReposResult.Success(it) }
                  .cast(LoadingReposResult::class.java)
                  .startWith(LoadingReposResult.InFlight)
                  .onErrorReturn { LoadingReposResult.Failure(it) }

            is ReposAction.RefreshRepos -> Observable.just(LoadingReposResult.DUMMY)//TODO
            is ReposAction.GetMoreItems -> Observable.just(LoadingReposResult.DUMMY)//TODO
            is ReposAction.BookmarkRepo -> Observable.just(LoadingReposResult.DUMMY)//TODO
          }
        }
  }
}