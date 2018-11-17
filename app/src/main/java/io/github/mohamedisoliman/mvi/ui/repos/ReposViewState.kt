package io.github.mohamedisoliman.mvi.ui.repos

import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.github.mohamedisoliman.mvi.mvibase.MviViewState

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
sealed class ReposViewState : MviViewState {
  object Idle : ReposViewState()
  data class Success(val repos: List<RepositoryItem>) : ReposViewState()
  data class MoreItemsSuccess(val repos: List<RepositoryItem>) : ReposViewState()
  data class Failure(val throwable: Throwable) : ReposViewState()
  data class SaveRepoSuccess(val message: String) : ReposViewState()
  object Loading : ReposViewState()
}