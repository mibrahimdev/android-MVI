package io.github.mohamedisoliman.mvi.githubrepos.data

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
data class RepositoryItem(
        val name: String,
        val description: String,
        val owner: RepositoryOwner
)