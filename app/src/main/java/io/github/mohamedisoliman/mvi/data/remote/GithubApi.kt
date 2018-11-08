package io.github.mohamedisoliman.mvi.data.remote

import io.github.mohamedisoliman.mvi.data.entities.GithubRepository
import io.reactivex.Observable
import retrofit2.http.GET

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
interface GithubApi {

    @GET("repositories")
    fun getRepositories(): Observable<List<GithubRepository>>

}