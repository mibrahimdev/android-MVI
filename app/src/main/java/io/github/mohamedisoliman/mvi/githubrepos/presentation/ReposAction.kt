package io.github.mohamedisoliman.mvi.githubrepos.presentation

import io.github.mohamedisoliman.mvi.githubrepos.mvibase.MviAction

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
sealed class ReposAction : MviAction {
    object InitialAction : ReposAction()
    data class GetMoreItems(val lastId: Int) : ReposAction()
    data class BookmarkRepo(val repoId: Int)
}