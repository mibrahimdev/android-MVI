package io.github.mohamedisoliman.mvi.githubrepos.data.remote

import io.github.mohamedisoliman.mvi.githubrepos.data.entities.GithubRepository
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
interface GithubApi {

    @GET("repositories")
    fun getRepositories(): Observable<List<GithubRepository>>

}