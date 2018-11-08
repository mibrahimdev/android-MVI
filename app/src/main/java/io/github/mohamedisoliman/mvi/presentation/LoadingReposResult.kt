package io.github.mohamedisoliman.mvi.presentation

import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.github.mohamedisoliman.mvi.mvibase.MviResult

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */

sealed class LoadingReposResult : MviResult {
  data class Success(val reposList: List<RepositoryItem>) : LoadingReposResult()
  data class Failure(val throwable: Throwable) : LoadingReposResult()
  object InFlight : LoadingReposResult()
  object DUMMY : LoadingReposResult()
}