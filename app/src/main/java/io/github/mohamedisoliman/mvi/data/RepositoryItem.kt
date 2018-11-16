package io.github.mohamedisoliman.mvi.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 *
 * Created by Mohamed Ibrahim on 10/19/18.
 */
@Entity
data class RepositoryItem(
  @PrimaryKey val id: Long,
  val name: String = "",
  val description: String = "",
  @Embedded(prefix = "owner_") val owner: RepositoryOwner = RepositoryOwner("", "")
)