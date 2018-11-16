package io.github.mohamedisoliman.mvi.ui.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.github.mohamedisoliman.mvi.R
import io.github.mohamedisoliman.mvi.data.RepositoryItem
import io.github.mohamedisoliman.mvi.ui.adapter.ReposAdapter.RepoHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_repos_recycler.view.bookmark
import kotlinx.android.synthetic.main.item_repos_recycler.view.owner_avatar
import kotlinx.android.synthetic.main.item_repos_recycler.view.repo_name

/**
 *
 * Created by Mohamed Ibrahim on 10/21/18.
 */
class ReposAdapter(private val bookmarkItemObservable: PublishSubject<RepositoryItem>) :
    RecyclerView.Adapter<RepoHolder>() {

  var repos: List<RepositoryItem> = emptyList()
    set(value) {
      val diffCallback =
        RepositoryItemsDiff(this.repos, value)
      val diffResult = DiffUtil.calculateDiff(diffCallback)
      field = value
      diffResult.dispatchUpdatesTo(this)
    }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): RepoHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.item_repos_recycler, parent, false)
    return RepoHolder(view)
  }

  override fun getItemCount(): Int = repos.size

  override fun onBindViewHolder(
    holder: RepoHolder,
    position: Int
  ) {
    holder.bind(repos[position])
    holder.bookMarkButtonClick(repos[position]) { bookmarkItemObservable.onNext(it) }
  }

  class RepoHolder(private val itemview: View) : RecyclerView.ViewHolder(itemview) {

    fun bind(repositoryItem: RepositoryItem) {
      itemview.repo_name.text = repositoryItem.name
      Picasso.get()
          .load(repositoryItem.owner.avatar)
          .into(itemview.owner_avatar)
    }

    fun bookMarkButtonClick(
      repoItem: RepositoryItem,
      function: (item: RepositoryItem) -> Unit
    ) {
      itemview.bookmark.setOnClickListener { function(repoItem) }
    }

  }
}