package io.github.mohamedisoliman.mvi.githubrepos.presentation

import io.github.mohamedisoliman.mvi.githubrepos.data.RepositoryItem
import io.github.mohamedisoliman.mvi.githubrepos.mvibase.MviAction
import io.github.mohamedisoliman.mvi.githubrepos.mvibase.MviResult

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */

sealed class LoadingReposResult : MviResult {
    data class Success(val reposList: List<RepositoryItem>) : LoadingReposResult()
    data class Failure(val throwable: Throwable) : LoadingReposResult()
    object InFlight : LoadingReposResult()
}