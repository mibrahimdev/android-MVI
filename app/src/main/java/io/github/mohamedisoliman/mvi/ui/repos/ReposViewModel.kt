package io.github.mohamedisoliman.mvi.ui.repos

import android.arch.lifecycle.ViewModel
import io.github.mohamedisoliman.mvi.MVIApp
import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.mvibase.MviViewModel
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.Idle
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult.Failure
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult.InFlight
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult.MoreItemSuccess
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult.SaveRepoSuccess
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult.Success
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.Loading
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.MoreItemsSuccess
import io.github.mohamedisoliman.mvi.ui.repos.ReposAction.GetMoreItems
import io.github.mohamedisoliman.mvi.ui.repos.ReposAction.InitialAction
import io.github.mohamedisoliman.mvi.ui.repos.ReposIntent.BookmarkRepo
import io.github.mohamedisoliman.mvi.ui.repos.ReposIntent.GetMoreRepos
import io.github.mohamedisoliman.mvi.ui.repos.ReposIntent.InitialLoadRepos
import io.github.mohamedisoliman.mvi.ui.repos.ReposIntent.RefreshRepos
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
      is SaveRepoSuccess -> ReposViewState.SaveRepoSuccess(
          result.message
      )
    }
  }

  private fun intentsToActions(intent: ReposIntent): ReposAction =
    when (intent) {
      is RefreshRepos -> ReposAction.RefreshRepos
      is InitialLoadRepos -> InitialAction
      is GetMoreRepos -> GetMoreItems(
          intent.lastId
      )
      is BookmarkRepo -> ReposAction.BookmarkRepo(
          intent.repositoryItem
      )
    }

  override fun states(): Observable<ReposViewState> {
    return intentsSubject.map { intentsToActions(it) }
        .compose { actions ->
          actions.publish { shared ->
            Observable.merge<GithubReposResult>(
                shared.ofType(InitialAction::class.java).flatMap {
                  GetGithubRepos(reposRepistory).execute(it)
                },
                shared.ofType(ReposAction.RefreshRepos::class.java).flatMap {
                  GetGithubRepos(reposRepistory).execute(it)
                },
                shared.ofType(GetMoreItems::class.java).flatMap {
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
            Idle
        ) { previousState: ReposViewState, result: GithubReposResult ->
          resultToViewStates(result, previousState)
        }
        .observeOn(AndroidSchedulers.mainThread())
  }
}