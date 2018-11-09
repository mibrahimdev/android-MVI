package io.github.mohamedisoliman.mvi.ui.adapter

import android.support.v7.util.DiffUtil
import io.github.mohamedisoliman.mvi.data.RepositoryItem

/**
 *
 * Created by Mohamed Ibrahim on 11/9/18.
 */
class RepositoryItemsDiff(
  val oldList: List<RepositoryItem>,
  val newList: List<RepositoryItem>
) : DiffUtil.Callback() {

  override fun areItemsTheSame(
    p0: Int,
    p1: Int
  ): Boolean = oldList[p0] == newList[p1]

  override fun getOldListSize(): Int = oldList.size

  override fun getNewListSize(): Int = newList.size

  override fun areContentsTheSame(
    p0: Int,
    p1: Int
  ): Boolean = oldList[p0] == newList[p1]
}