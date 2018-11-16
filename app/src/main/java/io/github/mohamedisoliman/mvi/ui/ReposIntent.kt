package io.github.mohamedisoliman.mvi.ui

import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.github.mohamedisoliman.mvi.mvibase.MviIntent

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
sealed class ReposIntent : MviIntent {
  object InitialLoadRepos : ReposIntent()
  object RefreshRepos : ReposIntent()
  data class GetMoreRepos(val lastId: Long) : ReposIntent()
  data class BookmarkRepo(val repositoryItem: RepositoryItem) : ReposIntent()
}