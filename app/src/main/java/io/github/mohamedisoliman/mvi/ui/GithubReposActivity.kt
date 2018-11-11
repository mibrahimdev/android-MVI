package io.github.mohamedisoliman.mvi.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import io.github.mohamedisoliman.mvi.R
import io.github.mohamedisoliman.mvi.endlessScrollObservable
import io.github.mohamedisoliman.mvi.mvibase.MviView
import io.github.mohamedisoliman.mvi.refreshObservable
import io.github.mohamedisoliman.mvi.ui.adapter.ReposAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_github_repos.recycler_view_repos
import kotlinx.android.synthetic.main.activity_github_repos.swipe_refresh_layout

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
    reposViewModel = ViewModelProviders.of(this)
        .get(ReposViewModel::class.java)

    setupRecyclerView()
    bind()
  }

  private fun bind() {
    reposViewModel.states()
        .subscribe { render(it) }
    reposViewModel.processIntents(intents())
  }

  override fun intents(): Observable<ReposIntent> =
    Observable.merge(initialIntent(), refreshReposIntent(), getMoreData())

  private fun initialIntent(): Observable<ReposIntent> =
    Observable.just(ReposIntent.InitialLoadRepos)

  private fun refreshReposIntent(): Observable<ReposIntent> =
    swipe_refresh_layout.refreshObservable()
        .map { ReposIntent.RefreshRepos }

  private fun getMoreData(): Observable<ReposIntent> =
    recycler_view_repos.endlessScrollObservable().map {
      ReposIntent.GetMoreRepos
    }

  private fun setupRecyclerView() {
    recycler_view_repos.layoutManager = LinearLayoutManager(this)
    recycler_view_repos.setHasFixedSize(true)
    recycler_view_repos.addItemDecoration(
        DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    )
    recycler_view_repos.adapter = reposAdapter
  }

  override fun render(state: ReposViewState) {
    when (state) {
      is ReposViewState.Loading -> {
        swipe_refresh_layout.isRefreshing = true
      }
      is ReposViewState.Idle -> {
        swipe_refresh_layout.isRefreshing = false
      }
      is ReposViewState.Success -> {
        swipe_refresh_layout.isRefreshing = false
        reposAdapter.repos = state.repos
      }

      is ReposViewState.MoreItemsSuccess -> {
        swipe_refresh_layout.isRefreshing = false
        reposAdapter.repos += state.repos
      }

      is ReposViewState.Failure -> {
        swipe_refresh_layout.isRefreshing = false
        Toast.makeText(this, "${state.throwable.message}", Toast.LENGTH_LONG)
            .show()
      }

      is ReposViewState.DUMMY -> {
        Toast.makeText(this, "DUMMY", Toast.LENGTH_LONG)
            .show()
      }

    }
  }

}