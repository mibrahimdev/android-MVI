package io.github.mohamedisoliman.mvi.ui.repos

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import io.github.mohamedisoliman.mvi.R
import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.github.mohamedisoliman.mvi.endlessScrollObservable
import io.github.mohamedisoliman.mvi.longToast
import io.github.mohamedisoliman.mvi.mvibase.MviView
import io.github.mohamedisoliman.mvi.refreshObservable
import io.github.mohamedisoliman.mvi.ui.adapter.ReposAdapter
import io.github.mohamedisoliman.mvi.ui.repos.ReposIntent.BookmarkRepo
import io.github.mohamedisoliman.mvi.ui.repos.ReposIntent.GetMoreRepos
import io.github.mohamedisoliman.mvi.ui.repos.ReposIntent.InitialLoadRepos
import io.github.mohamedisoliman.mvi.ui.repos.ReposIntent.RefreshRepos
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.Failure
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.Idle
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.Loading
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.MoreItemsSuccess
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.SaveRepoSuccess
import io.github.mohamedisoliman.mvi.ui.repos.ReposViewState.Success
import io.github.mohamedisoliman.mvi.ui.savedrepos.SavedReposActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_github_repos.recycler_view_repos
import kotlinx.android.synthetic.main.activity_github_repos.swipe_refresh_layout

/**
 *
 * Created by Mohamed Ibrahim on 10/20/18.
 */
class GithubReposActivity : AppCompatActivity(), MviView<ReposIntent, ReposViewState> {

  lateinit var reposViewModel: ReposViewModel
  private val bookmarkItemObservable = PublishSubject.create<RepositoryItem>()
  private val reposAdapter = ReposAdapter(bookmarkItemObservable)

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
    Observable.merge(initialIntent(), refreshReposIntent(), getMoreData(), bookMarkRepo())

  private fun initialIntent(): Observable<ReposIntent> =
    Observable.just(InitialLoadRepos)

  private fun refreshReposIntent(): Observable<ReposIntent> =
    swipe_refresh_layout.refreshObservable()
        .map { RefreshRepos }

  private fun getMoreData(): Observable<ReposIntent> =
    recycler_view_repos.endlessScrollObservable().map {
      GetMoreRepos(reposAdapter.repos.last().id)
    }

  private fun bookMarkRepo(): Observable<ReposIntent> {
    return bookmarkItemObservable.map {
      BookmarkRepo(
          it
      )
    }
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
      is Loading -> {
        swipe_refresh_layout.isRefreshing = true
      }
      is Idle -> {
        swipe_refresh_layout.isRefreshing = false
      }
      is Success -> {
        swipe_refresh_layout.isRefreshing = false
        reposAdapter.repos = state.repos
      }

      is MoreItemsSuccess -> {
        swipe_refresh_layout.isRefreshing = false
        reposAdapter.repos += state.repos
      }

      is Failure -> {
        swipe_refresh_layout.isRefreshing = false
        longToast("${state.throwable.message}")
      }

      is SaveRepoSuccess -> {
        longToast(state.message)
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.repos_activity_menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.saved_repos) navigateToSavedRepos()
    return super.onOptionsItemSelected(item)
  }

  private fun navigateToSavedRepos() {
    val intent = Intent(this, SavedReposActivity::class.java)
    startActivity(intent)
  }

}