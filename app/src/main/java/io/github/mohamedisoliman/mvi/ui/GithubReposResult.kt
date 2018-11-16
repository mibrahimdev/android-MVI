package io.github.mohamedisoliman.mvi.ui

import io.github.mohamedisoliman.mvi.data.RepositoryItem

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */

sealed class GithubReposResult {
  data class Success(val reposList: List<RepositoryItem>) : GithubReposResult()
  data class MoreItemSuccess(val reposList: List<RepositoryItem>) : GithubReposResult()
  data class SaveRepoSuccess(val message: String) : GithubReposResult()
  data class Failure(val throwable: Throwable) : GithubReposResult()
  object InFlight : GithubReposResult()
  object DUMMY : GithubReposResult()
}