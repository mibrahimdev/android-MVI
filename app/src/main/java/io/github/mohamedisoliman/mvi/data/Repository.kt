package io.github.mohamedisoliman.mvi.data

import io.github.mohamedisoliman.mvi.data.local.ReposLocalStore
import io.github.mohamedisoliman.mvi.data.remote.GithubApi
import io.github.mohamedisoliman.mvi.ui.GithubReposResult
import io.github.mohamedisoliman.mvi.ui.GithubReposResult.SaveRepoSuccess
import io.reactivex.Observable
import timber.log.Timber

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
class Repository(
  private val githubApi: GithubApi,
  private val reposLocalStore: ReposLocalStore
) {

  fun getGithubRepositories(since: Long): Observable<List<RepositoryItem>> =
    githubApi.getRepositories(since)
        .flatMapIterable { it }
        .doOnNext { Timber.i(it?.toString()) }
        .map {
          RepositoryItem(
              it.id,
              it.name,
              it.description ?: "EMPTY",
              RepositoryOwner(
                  it.owner.login ?: "EMPTY", it.owner.avatarUrl ?: ""
              )
          )
        }
        .toList()
        .toObservable()
        .doOnError { Timber.e(it) }

  fun saveRepo(repositoryItem: RepositoryItem): Observable<GithubReposResult> {
    return reposLocalStore.insert(repositoryItem)
        .andThen(
            Observable.just<GithubReposResult>(
                SaveRepoSuccess("${repositoryItem.name} has been saved")
            )
        )
  }
}