package io.github.mohamedisoliman.mvi.ui

import android.arch.lifecycle.ViewModel
import io.github.mohamedisoliman.mvi.MVIApp
import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.mvibase.MviViewModel
import io.github.mohamedisoliman.mvi.ui.GithubReposResult.DUMMY
import io.github.mohamedisoliman.mvi.ui.GithubReposResult.Failure
import io.github.mohamedisoliman.mvi.ui.GithubReposResult.InFlight
import io.github.mohamedisoliman.mvi.ui.GithubReposResult.MoreItemSuccess
import io.github.mohamedisoliman.mvi.ui.GithubReposResult.SaveRepoSuccess
import io.github.mohamedisoliman.mvi.ui.GithubReposResult.Success
import io.github.mohamedisoliman.mvi.ui.ReposViewState.Loading
import io.github.mohamedisoliman.mvi.ui.ReposViewState.MoreItemsSuccess
import io.github.mohamedisoliman.mvi.usecase.GetGithubRepos
import io.github.mohamedisoliman.mvi.usecase.SaveRepo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 *
 * Created by Mohamed Ibrahim on 10/20/18.
 */
class ReposViewModel : ViewModel(), MviViewModel<ReposIntent, ReposViewState> {

  val reposRepistory: Repository by lazy { MVIApp.appDependencies.reposRepository }
  private val intentsSubject = PublishSubject.create<ReposIntent>()

  override fun processIntents(intents: Observable<ReposIntent>) {
    intents.subscribe(intentsSubject)
  }

  private fun resultToViewStates(
    result: GithubReposResult,
    previousState: ReposViewState
  ): ReposViewState {
    return when (result) {
      is InFlight -> Loading
      is Success -> ReposViewState.Success(result.reposList)
      is MoreItemSuccess -> MoreItemsSuccess(result.reposList)
      is Failure -> ReposViewState.Failure(result.throwable)
      is DUMMY -> ReposViewState.DUMMY
      is SaveRepoSuccess -> ReposViewState.SaveRepoSuccess(result.message)
    }
  }

  private fun intentsToActions(intent: ReposIntent): ReposAction =
    when (intent) {
      is ReposIntent.RefreshRepos -> ReposAction.RefreshRepos
      is ReposIntent.InitialLoadRepos -> ReposAction.InitialAction
      is ReposIntent.GetMoreRepos -> ReposAction.GetMoreItems(intent.lastId)
      is ReposIntent.BookmarkRepo -> ReposAction.BookmarkRepo(intent.repositoryItem)
    }

  override fun states(): Observable<ReposViewState> {
    return intentsSubject.map { intentsToActions(it) }
        .compose { actions ->
          actions.publish { shared ->
            Observable.merge<GithubReposResult>(
                shared.ofType(ReposAction.InitialAction::class.java).flatMap {
                  GetGithubRepos(reposRepistory).execute(it)
                },
                shared.ofType(ReposAction.RefreshRepos::class.java).flatMap {
                  GetGithubRepos(reposRepistory).execute(it)
                },
                shared.ofType(ReposAction.GetMoreItems::class.java).flatMap {
                  GetGithubRepos(reposRepistory).execute(it)
                },
                shared.ofType(ReposAction.BookmarkRepo::class.java).flatMap {
                  SaveRepo(reposRepistory).execute(it)
                }
            )
          }
        }
        .distinctUntilChanged()
        .scan<ReposViewState>(
            ReposViewState.Idle
        ) { previousState: ReposViewState, result: GithubReposResult ->
          resultToViewStates(result, previousState)
        }
        .observeOn(AndroidSchedulers.mainThread())
  }
}