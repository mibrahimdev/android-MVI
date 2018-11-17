package io.github.mohamedisoliman.mvi.usecase

import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult.Failure
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult.InFlight
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult.Success
import io.github.mohamedisoliman.mvi.ui.repos.ReposAction
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
class GetGithubRepos(private val repository: Repository) {

  fun execute(action: ReposAction): Observable<GithubReposResult> {
    return Observable.just(action)
        .subscribeOn(Schedulers.io())
        .flatMap { incomingActions ->
          when (incomingActions) {
            is ReposAction.InitialAction -> getRepos()
            is ReposAction.RefreshRepos -> getRepos()//just refresh the list
            is ReposAction.GetMoreItems -> getRepos(incomingActions.lastId)
            is ReposAction.BookmarkRepo -> repository.saveRepo(incomingActions.repositoryItem)
          }
        }
  }

  private fun getRepos(since: Long = 1): Observable<GithubReposResult>? {
    return repository.getGithubRepositories(since)
        .map<GithubReposResult> {
          if (since == 1L) Success(it) else GithubReposResult.MoreItemSuccess(it)
        }
        .cast(GithubReposResult::class.java)
        .startWith(InFlight)
        .onErrorReturn { Failure(it) }
  }
}