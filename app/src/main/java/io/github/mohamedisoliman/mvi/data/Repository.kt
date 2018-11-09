package io.github.mohamedisoliman.mvi.data

import io.github.mohamedisoliman.mvi.data.remote.GithubApi
import io.reactivex.Observable
import timber.log.Timber

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
class Repository(private val githubApi: GithubApi) {

  fun getGithubRepositories(since: Long): Observable<List<RepositoryItem>> =
    githubApi.getRepositories(since)
        .flatMapIterable { it }
        .doOnNext { Timber.i(it?.toString()) }
        .map {
          RepositoryItem(
              it.name,
              it.description ?: "EMPTY",
              RepositoryOwner(
                  it.owner.login ?: "EMPTY", it.owner.avatarUrl
                  ?: ""
              )
          )
        }
        .toList()
        .toObservable()
        .doOnError { Timber.e(it) }
}