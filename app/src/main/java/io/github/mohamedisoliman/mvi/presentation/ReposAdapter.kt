package io.github.mohamedisoliman.mvi.presentation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.github.mohamedisoliman.mvi.R
import io.github.mohamedisoliman.mvi.data.RepositoryItem
import kotlinx.android.synthetic.main.item_repos_recycler.view.*

/**
 *
 * Created by Mohamed Ibrahim on 10/21/18.
 */
class ReposAdapter : RecyclerView.Adapter<ReposAdapter.RepoHolder>() {


    var repos: List<RepositoryItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_repos_recycler, parent, false)
        return RepoHolder(view)
    }

    override fun getItemCount(): Int = repos.size

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        holder.bind(repos[position])

    }

    class RepoHolder(private val itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun bind(repositoryItem: RepositoryItem) {
            itemview.repo_name.text = repositoryItem.name
            itemview.bookmark.setOnClickListener {
                Toast.makeText(itemview.context, "SAVED FAKELY", Toast.LENGTH_LONG).show()
            }
            Picasso.get().load(repositoryItem.owner.avatar).into(itemview.owner_avatar)
        }

    }
}