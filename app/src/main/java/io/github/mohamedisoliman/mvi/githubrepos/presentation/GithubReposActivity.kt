package io.github.mohamedisoliman.mvi.githubrepos.presentation

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import io.github.mohamedisoliman.mvi.R
import io.github.mohamedisoliman.mvi.githubrepos.mvibase.MviView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_github_repos.*
import timber.log.Timber

/**
 *
 * Created by Mohamed Ibrahim on 10/20/18.
 */
class GithubReposActivity : AppCompatActivity(), MviView<ReposIntent, ReposViewState> {

    private val reposAdapter = ReposAdapter()
    lateinit var reposViewModel: ReposViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repos)
        reposViewModel = ViewModelProviders.of(this).get(ReposViewModel::class.java)

        recycler_view_repos.layoutManager = LinearLayoutManager(this)
        recycler_view_repos.setHasFixedSize(true)
        recycler_view_repos.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_view_repos.adapter = reposAdapter

        //TEST regular way
        swipe_refresh_layout.isRefreshing = true
        reposViewModel.loadRepos().subscribe({
            reposAdapter.repos = it
            swipe_refresh_layout.isRefreshing = false
        }, {
            swipe_refresh_layout.isRefreshing = false
            Timber.e(it)
        }, {})

    }

    override fun intents(): Observable<ReposIntent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(state: ReposViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}