package io.github.mohamedisoliman.mvi.usecase

import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.ui.repos.GithubReposResult
import io.github.mohamedisoliman.mvi.ui.repos.ReposAction
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by Mohamed Ibrahim on 11/12/18.
 */
class SaveRepo(private val repository: Repository) {

  fun execute(bookmarkRepoAction: ReposAction.BookmarkRepo): Observable<GithubReposResult> =
    repository.saveRepo(bookmarkRepoAction.repositoryItem)
        .subscribeOn(Schedulers.io())

}