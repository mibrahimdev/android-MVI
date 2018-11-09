package io.github.mohamedisoliman.mvi.ui

import android.arch.lifecycle.ViewModel
import io.github.mohamedisoliman.mvi.MVIApp
import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.mvibase.MviViewModel
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult.DUMMY
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult.Failure
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult.InFlight
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult.MoreItemSuccess
import io.github.mohamedisoliman.mvi.ui.LoadingReposResult.Success
import io.github.mohamedisoliman.mvi.ui.ReposViewState.Loading
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

  private var lastId: Long = -1

  override fun processIntents(intents: Observable<ReposIntent>) {
    intents.subscribe(intentsSubject)
  }

  private fun resultToViewStates(
    result: LoadingReposResult,
    previousState: ReposViewState
  ): ReposViewState {
    return when (result) {
      is InFlight -> Loading
      is Success -> ReposViewState.Success(result.reposList).also {
        lastId = it.repos.lastIndex.toLong()
      }
      is MoreItemSuccess -> if (previousState is ReposViewState.Success) {
        val list = previousState.repos
        list.toMutableList()
            .addAll(result.reposList)//ADD new Items
        lastId = list.lastIndex.toLong()
        ReposViewState.Success(list)
      } else ReposViewState.Failure(Throwable("Failed to get New Items"))
      is Failure -> ReposViewState.Failure(result.throwable)
      is DUMMY -> ReposViewState.DUMMY
    }
  }

  private fun intentsToActions(intent: ReposIntent): ReposAction =
    when (intent) {
      is ReposIntent.RefreshRepos -> ReposAction.RefreshRepos
      is ReposIntent.InitialLoadRepos -> ReposAction.InitialAction
      is ReposIntent.GetMoreRepos -> ReposAction.GetMoreItems(lastId)
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
                shared.ofType(ReposAction.RefreshRepos::class.java).flatMap {
                  GetGithubRepos(reposRepistory).execute(it)
                },
                shared.ofType(ReposAction.GetMoreItems::class.java).flatMap {
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