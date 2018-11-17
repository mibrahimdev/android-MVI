package io.github.mohamedisoliman.mvi.ui.savedrepos

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import io.github.mohamedisoliman.mvi.R
import io.github.mohamedisoliman.mvi.ui.adapter.ReposAdapter
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_github_repos.recycler_view_repos
import kotlinx.android.synthetic.main.activty_saved_repos.recycler_view_saved_repos

/**
 * Not an MVI screen, just to show the Local Repos
 * Created by Mohamed Ibrahim on 11/17/18.
 */
class SavedReposActivity : AppCompatActivity() {

  private val reposAdapter = ReposAdapter(PublishSubject.create())

  @SuppressLint("CheckResult")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activty_saved_repos)
    supportActionBar?.title = "Saved Repos"
    setupRecyclerView()

    val savedReposViewModel = ViewModelProviders.of(this)
        .get(SavedReposViewModel::class.java)
    savedReposViewModel.getSavedRepos()
        .subscribe { reposAdapter.repos = it }
  }

  private fun setupRecyclerView() {
    recycler_view_saved_repos.layoutManager = LinearLayoutManager(this)
    recycler_view_saved_repos.setHasFixedSize(true)
    recycler_view_saved_repos.addItemDecoration(
        DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    )
    recycler_view_saved_repos.adapter = reposAdapter
  }


}