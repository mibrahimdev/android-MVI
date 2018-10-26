package io.github.mohamedisoliman.mvi.githubrepos.presentation

import io.github.mohamedisoliman.mvi.githubrepos.mvibase.MviIntent

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
sealed class ReposIntent : MviIntent {
    object LoadRepos : ReposIntent()
    data class LoadMoreRepos(val lastId: Int) : ReposIntent()
    data class BookmarkRepo(val repoId: Int) : ReposIntent()
}