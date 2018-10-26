package io.github.mohamedisoliman.mvi.githubrepos.data

import io.github.mohamedisoliman.mvi.githubrepos.data.remote.GithubApi
import timber.log.Timber

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
class Repository(private val githubApi: GithubApi) {

    fun getGithubRepositories() =
            githubApi.getRepositories()
                    .flatMapIterable { it }
                    .doOnNext { Timber.i(it?.toString()) }
                    .map {
                        RepositoryItem(
                                it.name,
                                it.description ?: "EMPTY",
                                RepositoryOwner(it.owner.login ?: "EMPTY", it.owner.avatarUrl
                                        ?: ""))
                    }
                    .toList()
                    .toObservable()
                    .doOnError { Timber.e(it) }
}