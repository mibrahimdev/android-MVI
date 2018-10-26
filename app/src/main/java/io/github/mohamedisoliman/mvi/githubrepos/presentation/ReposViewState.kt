package io.github.mohamedisoliman.mvi.githubrepos.presentation

import io.github.mohamedisoliman.mvi.githubrepos.data.RepositoryItem
import io.github.mohamedisoliman.mvi.githubrepos.mvibase.MviViewState

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
sealed class ReposViewState : MviViewState {
    data class Success(val repos: List<RepositoryItem>) : ReposViewState()
    data class Failure(val throwable: Throwable) : ReposViewState()
    object Loading : ReposViewState()
}