package io.github.mohamedisoliman.mvi.presentation

import android.arch.lifecycle.ViewModel
import io.github.mohamedisoliman.mvi.MVIApp
import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.mvibase.MviViewModel
import io.github.mohamedisoliman.mvi.presentation.LoadingReposResult.DUMMY
import io.github.mohamedisoliman.mvi.presentation.LoadingReposResult.Failure
import io.github.mohamedisoliman.mvi.presentation.LoadingReposResult.InFlight
import io.github.mohamedisoliman.mvi.presentation.LoadingReposResult.Success
import io.github.mohamedisoliman.mvi.presentation.ReposViewState.Loading
import io.github.mohamedisoliman.mvi.usecase.GetGithubRepos
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 *
 * Created by Mohamed Ibrahim on 10/20/18.
 */
class ReposViewModel : ViewModel(), MviViewModel<ReposIntent, ReposViewState> {

  val reposRepistory: Repository by lazy { MVIApp.appDependencies.reposRepository }
  private val renderer = BehaviorSubject.create<ReposViewState>()
  private val intentsSubject = PublishSubject.create<ReposIntent>()

  override fun processIntents(intents: Observable<ReposIntent>) {
    intents.subscribe(intentsSubject)
  }

  private fun resultToViewStates(
    result: LoadingReposResult,
    previousState: ReposViewState
  ): ReposViewState {
    return when (result) {
      is InFlight -> {
        previousState
        Loading
      }
      is Success -> ReposViewState.Success(result.reposList)
      is Failure -> ReposViewState.Failure(result.throwable)
      is DUMMY -> ReposViewState.DUMMY
    }
  }

  private fun intentsToActions(intent: ReposIntent): ReposAction =
    when (intent) {
      is ReposIntent.RefreshRepos -> ReposAction.RefreshRepos
      is ReposIntent.InitialLoadRepos -> ReposAction.InitialAction
      is ReposIntent.LoadMoreRepos -> ReposAction.GetMoreItems(intent.lastId)
      is ReposIntent.BookmarkRepo -> ReposAction.BookmarkRepo(intent.repoId)
    }

  override fun states(): Observable<ReposViewState> {
    return intentsSubject.map { intentsToActions(it) }
        .compose { actions ->
          actions.publish { shared ->
            Observable.merge<LoadingReposResult>(
                shared.ofType(ReposAction.InitialAction::class.java).flatMap {
                  GetGithubRepos(reposRepistory).execute(it)
                },
                shared.ofType(ReposAction.BookmarkRepo::class.java).flatMap {
                  GetGithubRepos(reposRepistory).execute(it)
                }
            )
          }
        }
        .distinctUntilChanged()
        .scan<ReposViewState>(
            ReposViewState.Idle
        ) { previousState: ReposViewState, result: LoadingReposResult ->
          resultToViewStates(result, previousState)
        }
        .observeOn(AndroidSchedulers.mainThread())
  }
}