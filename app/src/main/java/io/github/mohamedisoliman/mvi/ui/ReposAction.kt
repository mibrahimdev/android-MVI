package io.github.mohamedisoliman.mvi.ui

import io.github.mohamedisoliman.mvi.mvibase.MviAction

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
sealed class ReposAction : MviAction {
    object InitialAction : ReposAction()
    data class GetMoreItems(val lastId: Long) : ReposAction()
    data class BookmarkRepo(val repoId: Int) : ReposAction()
    object RefreshRepos : ReposAction()
}