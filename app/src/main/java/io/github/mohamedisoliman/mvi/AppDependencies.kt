package io.github.mohamedisoliman.mvi

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.github.mohamedisoliman.mvi.data.Repository
import io.github.mohamedisoliman.mvi.data.remote.GithubApi
import io.github.mohamedisoliman.mvi.data.remote.RemoteFactory
import timber.log.Timber

/**
 *
 * Created by Mohamed Ibrahim on 10/26/18.
 */

class AppDependencies(val app: Application) : AndroidViewModel(app) {

    init {
        Timber.plant(Timber.DebugTree());
    }

    val reposRepository by lazy { createReposRepository() }
    private val githubApi by lazy { getGithubRemote() }
    private fun getGithubRemote(): GithubApi = RemoteFactory.newGithubApi()
    private fun createReposRepository(): Repository = Repository(getGithubRemote())


}